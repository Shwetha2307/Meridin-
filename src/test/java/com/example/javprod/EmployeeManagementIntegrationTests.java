package com.example.javprod;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@WithMockUser(username = "admin", roles = {"ADMIN"})
class EmployeeManagementIntegrationTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .apply(springSecurity())
            .build();
    }

    @Test
    @DisplayName("POST /api/auth/login authenticates valid user and returns role + permissions")
    @WithAnonymousUser
    void shouldLoginWithValidCredentials() throws Exception {
        String loginPayload = """
            {
                "username": "admin",
                "password": "admin123"
            }
            """;

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginPayload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username", is("admin")))
            .andExpect(jsonPath("$.displayName", is("Marcus Vance")))
            .andExpect(jsonPath("$.role", is("ADMIN")))
            .andExpect(jsonPath("$.permissions", hasItem("EMPLOYEE_WRITE")))
            .andExpect(jsonPath("$.permissions", hasItem("SALARY_VIEW")));
    }

    @Test
    @DisplayName("POST /api/auth/login rejects invalid password with 401 Unauthorized")
    @WithAnonymousUser
    void shouldRejectInvalidLoginCredentials() throws Exception {
        String invalidLogin = """
            {
                "username": "admin",
                "password": "wrongpassword"
            }
            """;

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidLogin))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.status", is(401)));
    }

    @Test
    @DisplayName("Unauthenticated request to /api/employees returns 401 Unauthorized")
    @WithAnonymousUser
    void shouldRequireAuthenticationForProtectedApi() throws Exception {
        mockMvc.perform(get("/api/employees").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.status", is(401)));
    }

    @Test
    @DisplayName("RBAC: EMPLOYEE role cannot create employees or approve leaves (403 Forbidden) and has others' salaries redacted")
    @WithMockUser(username = "employee", roles = {"EMPLOYEE"})
    void shouldEnforceRbacForEmployeeRole() throws Exception {
        String createPayload = """
            {
                "firstName": "Unauthorized",
                "lastName": "Hire",
                "email": "unauth.hire@enterprise.io",
                "phone": "+1 (555) 222-3333",
                "role": "Engineer",
                "hireDate": "2025-01-10",
                "salary": 110000.00,
                "status": "ACTIVE",
                "departmentId": 1
            }
            """;

        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createPayload))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.status", is(403)));

        mockMvc.perform(patch("/api/leaves/2/approve")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"remarks\": \"Attempted by employee\"}"))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.status", is(403)));

        // Employee #1 (Marcus Vance) salary should be redacted (null) for user 'employee' (Liam O'Connor)
        mockMvc.perform(get("/api/employees/1").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.salary").value(nullValue()));
    }

    @Test
    @DisplayName("GET /api/employees returns paginated directory with seeded records")
    void shouldReturnPaginatedEmployees() throws Exception {
        mockMvc.perform(get("/api/employees")
                .param("page", "0")
                .param("size", "10")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(greaterThanOrEqualTo(5))))
            .andExpect(jsonPath("$.page.totalElements", greaterThanOrEqualTo(10)))
            .andExpect(jsonPath("$.content[0].email", notNullValue()))
            .andExpect(jsonPath("$.content[0].departmentName", notNullValue()));
    }

    @Test
    @DisplayName("GET /api/employees with search query filters results")
    void shouldFilterEmployeesBySearchQuery() throws Exception {
        mockMvc.perform(get("/api/employees")
                .param("search", "Marcus")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].firstName", is("Marcus")))
            .andExpect(jsonPath("$.content[0].lastName", is("Vance")));
    }

    @Test
    @DisplayName("POST /api/employees creates employee and generates audit event")
    void shouldCreateEmployeeSuccessfully() throws Exception {
        String jsonPayload = """
            {
                "firstName": "Julian",
                "lastName": "Casablancas",
                "email": "julian.c@enterprise.io",
                "phone": "+1 (555) 888-1234",
                "role": "Lead Systems Architect",
                "hireDate": "2024-05-10",
                "salary": 165000.00,
                "status": "ACTIVE",
                "departmentId": 1,
                "managerId": 1
            }
            """;

        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id", notNullValue()))
            .andExpect(jsonPath("$.fullName", is("Julian Casablancas")))
            .andExpect(jsonPath("$.email", is("julian.c@enterprise.io")))
            .andExpect(jsonPath("$.departmentName", notNullValue()));
    }

    @Test
    @DisplayName("POST /api/employees fails validation on invalid email and blank name")
    void shouldRejectEmployeeWithInvalidFields() throws Exception {
        String invalidPayload = """
            {
                "firstName": "",
                "lastName": "Doe",
                "email": "not-an-email",
                "phone": "+1 (555) 000-0000",
                "role": "QA Engineer",
                "hireDate": "2024-01-01",
                "salary": -5000.00,
                "status": "ACTIVE",
                "departmentId": 1
            }
            """;

        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidPayload))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status", is(400)))
            .andExpect(jsonPath("$.error", is("Validation Failed")))
            .andExpect(jsonPath("$.validationErrors.firstName", notNullValue()))
            .andExpect(jsonPath("$.validationErrors.email", notNullValue()))
            .andExpect(jsonPath("$.validationErrors.salary", notNullValue()));
    }

    @Test
    @DisplayName("POST /api/employees rejects duplicate email with 409 Conflict")
    void shouldRejectDuplicateEmail() throws Exception {
        String duplicatePayload = """
            {
                "firstName": "Marcus",
                "lastName": "Duplicate",
                "email": "marcus.vance@enterprise.io",
                "phone": "+1 (555) 999-9999",
                "role": "Duplicate Role",
                "hireDate": "2024-01-01",
                "salary": 100000.00,
                "status": "ACTIVE",
                "departmentId": 1
            }
            """;

        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(duplicatePayload))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.status", is(409)));
    }

    @Test
    @DisplayName("Leave application and approval workflow transitions request to APPROVED")
    void shouldHandleLeaveApplicationAndApproval() throws Exception {
        // 1. Apply for leave
        String applyPayload = """
            {
                "employeeId": 1,
                "startDate": "2026-11-01",
                "endDate": "2026-11-05",
                "leaveType": "ANNUAL",
                "reason": "Q4 conference attendance and vacation"
            }
            """;

        String responseJson = mockMvc.perform(post("/api/leaves")
                .contentType(MediaType.APPLICATION_JSON)
                .content(applyPayload))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id", notNullValue()))
            .andExpect(jsonPath("$.status", is("PENDING")))
            .andExpect(jsonPath("$.daysCount", is(5)))
            .andReturn()
            .getResponse()
            .getContentAsString();

        // Extract ID from response (e.g. "id":6)
        long leaveId = extractIdFromResponse(responseJson);

        // 2. Approve the leave
        String approvalPayload = """
            {
                "remarks": "Approved by engineering VP"
            }
            """;

        mockMvc.perform(patch("/api/leaves/" + leaveId + "/approve")
                .contentType(MediaType.APPLICATION_JSON)
                .content(approvalPayload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is((int) leaveId)))
            .andExpect(jsonPath("$.status", is("APPROVED")));
    }

    @Test
    @DisplayName("Asset lifecycle: creation, checkout to employee, and return")
    void shouldTrackAssetLifecycle() throws Exception {
        // 1. Create asset
        String assetPayload = """
            {
                "assetTag": "AST-TST-999",
                "name": "Apple MacBook Air 15 M3",
                "category": "LAPTOP",
                "serialNumber": "SN-TST-AIR-15",
                "status": "IN_STOCK"
            }
            """;

        String assetJson = mockMvc.perform(post("/api/assets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(assetPayload))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.assetTag", is("AST-TST-999")))
            .andExpect(jsonPath("$.status", is("IN_STOCK")))
            .andReturn()
            .getResponse()
            .getContentAsString();

        long assetId = extractIdFromResponse(assetJson);

        // 2. Checkout to employee 2
        mockMvc.perform(post("/api/assets/" + assetId + "/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"employeeId\": 2}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status", is("ASSIGNED")))
            .andExpect(jsonPath("$.assignedEmployeeId", is(2)));

        // 3. Return asset
        mockMvc.perform(post("/api/assets/" + assetId + "/return"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status", is("IN_STOCK")))
            .andExpect(jsonPath("$.assignedEmployeeId", nullValue()));
    }

    @Test
    @DisplayName("GET /api/analytics/summary returns accurate organizational metrics")
    void shouldReturnAnalyticsSummary() throws Exception {
        mockMvc.perform(get("/api/analytics/summary")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalHeadcount", greaterThanOrEqualTo(10)))
            .andExpect(jsonPath("$.activeHeadcount", greaterThanOrEqualTo(8)))
            .andExpect(jsonPath("$.departmentDistribution", hasSize(greaterThanOrEqualTo(5))))
            .andExpect(jsonPath("$.recentActivities", hasSize(greaterThanOrEqualTo(1))))
            .andExpect(jsonPath("$.totalAssets", greaterThanOrEqualTo(10)));
    }

    private long extractIdFromResponse(String json) {
        int idx = json.indexOf("\"id\":");
        if (idx == -1) return 1L;
        int start = idx + 5;
        int end = json.indexOf(",", start);
        if (end == -1) end = json.indexOf("}", start);
        return Long.parseLong(json.substring(start, end).trim());
    }
}
