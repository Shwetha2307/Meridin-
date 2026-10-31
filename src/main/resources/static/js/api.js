/**
 * Enterprise Employee Management System (EMS) - API Client & Resilient Fallback Engine
 * Zero external libraries, async/await, fetch with offline mock fallback store.
 */

const FALLBACK_STORE = {
  departments: [
    { id: 1, name: "Engineering & Cloud Architecture", code: "ENG", description: "Core distributed systems, cloud infrastructure, AI models, and platform reliability.", employeeCount: 5 },
    { id: 2, name: "Human Resources & People Ops", code: "HR", description: "Talent acquisition, organizational development, total rewards, and culture.", employeeCount: 2 },
    { id: 3, name: "Corporate Finance & Treasury", code: "FIN", description: "Corporate financial planning, fiscal audits, payroll, and investor relations.", employeeCount: 2 },
    { id: 4, name: "Product Strategy & Design", code: "PRD", description: "User experience research, system interfaces, product roadmaps, and analytics.", employeeCount: 2 },
    { id: 5, name: "Global Sales & Growth", code: "MKT", description: "Enterprise solution sales, brand awareness, client partnerships, and demand gen.", employeeCount: 2 }
  ],
  employees: [
    { id: 1, firstName: "Marcus", lastName: "Vance", fullName: "Marcus Vance", email: "marcus.vance@enterprise.io", phone: "+1 (555) 019-2831", role: "VP of Software Engineering", hireDate: "2022-08-15", salary: 210000.00, status: "ACTIVE", departmentId: 1, departmentName: "Engineering & Cloud Architecture", departmentCode: "ENG", managerId: null, managerName: null },
    { id: 2, firstName: "Elena", lastName: "Rostova", fullName: "Elena Rostova", email: "elena.rostova@enterprise.io", phone: "+1 (555) 018-9942", role: "Director of People & Culture", hireDate: "2023-04-10", salary: 175000.00, status: "ACTIVE", departmentId: 2, departmentName: "Human Resources & People Ops", departmentCode: "HR", managerId: null, managerName: null },
    { id: 3, firstName: "David", lastName: "Sterling", fullName: "David Sterling", email: "david.sterling@enterprise.io", phone: "+1 (555) 014-5521", role: "Chief Financial Officer", hireDate: "2021-10-01", salary: 225000.00, status: "ACTIVE", departmentId: 3, departmentName: "Corporate Finance & Treasury", departmentCode: "FIN", managerId: null, managerName: null },
    { id: 4, firstName: "Sophia", lastName: "Lin", fullName: "Sophia Lin", email: "sophia.lin@enterprise.io", phone: "+1 (555) 017-3319", role: "VP of Product Experience", hireDate: "2023-12-01", salary: 195000.00, status: "ACTIVE", departmentId: 4, departmentName: "Product Strategy & Design", departmentCode: "PRD", managerId: null, managerName: null },
    { id: 5, firstName: "Carlos", lastName: "Mendoza", fullName: "Carlos Mendoza", email: "carlos.mendoza@enterprise.io", phone: "+1 (555) 012-7744", role: "VP of Enterprise Sales", hireDate: "2023-09-15", salary: 185000.00, status: "ACTIVE", departmentId: 5, departmentName: "Global Sales & Growth", departmentCode: "MKT", managerId: null, managerName: null },
    { id: 6, firstName: "Alexander", lastName: "Wright", fullName: "Alexander Wright", email: "alexander.wright@enterprise.io", phone: "+1 (555) 020-8812", role: "Principal Cloud Architect", hireDate: "2024-06-12", salary: 172000.00, status: "ACTIVE", departmentId: 1, departmentName: "Engineering & Cloud Architecture", departmentCode: "ENG", managerId: 1, managerName: "Marcus Vance" },
    { id: 7, firstName: "Maya", lastName: "Patel", fullName: "Maya Patel", email: "maya.patel@enterprise.io", phone: "+1 (555) 021-9934", role: "Senior Staff Security Engineer", hireDate: "2024-12-01", salary: 164000.00, status: "ACTIVE", departmentId: 1, departmentName: "Engineering & Cloud Architecture", departmentCode: "ENG", managerId: 1, managerName: "Marcus Vance" },
    { id: 8, firstName: "Lucas", lastName: "Bernard", fullName: "Lucas Bernard", email: "lucas.bernard@enterprise.io", phone: "+1 (555) 023-4411", role: "Senior Full-Stack Engineer", hireDate: "2025-07-20", salary: 132000.00, status: "ON_LEAVE", departmentId: 1, departmentName: "Engineering & Cloud Architecture", departmentCode: "ENG", managerId: 6, managerName: "Alexander Wright" },
    { id: 9, firstName: "Chloe", lastName: "Dubois", fullName: "Chloe Dubois", email: "chloe.dubois@enterprise.io", phone: "+1 (555) 025-6677", role: "Senior People Partner", hireDate: "2025-08-14", salary: 118000.00, status: "ACTIVE", departmentId: 2, departmentName: "Human Resources & People Ops", departmentCode: "HR", managerId: 2, managerName: "Elena Rostova" },
    { id: 10, firstName: "Nathan", lastName: "Drake", fullName: "Nathan Drake", email: "nathan.drake@enterprise.io", phone: "+1 (555) 026-1188", role: "Lead Financial Analyst", hireDate: "2025-04-10", salary: 138000.00, status: "ACTIVE", departmentId: 3, departmentName: "Corporate Finance & Treasury", departmentCode: "FIN", managerId: 3, managerName: "David Sterling" },
    { id: 11, firstName: "Aisha", lastName: "Al-Mansoor", fullName: "Aisha Al-Mansoor", email: "aisha.mansoor@enterprise.io", phone: "+1 (555) 028-5522", role: "Lead Design Systems Specialist", hireDate: "2026-02-15", salary: 152000.00, status: "ACTIVE", departmentId: 4, departmentName: "Product Strategy & Design", departmentCode: "PRD", managerId: 4, managerName: "Sophia Lin" },
    { id: 12, firstName: "Ryan", lastName: "Gallagher", fullName: "Ryan Gallagher", email: "ryan.gallagher@enterprise.io", phone: "+1 (555) 029-7711", role: "Senior Account Executive", hireDate: "2026-05-20", salary: 142000.00, status: "ACTIVE", departmentId: 5, departmentName: "Global Sales & Growth", departmentCode: "MKT", managerId: 5, managerName: "Carlos Mendoza" },
    { id: 13, firstName: "Liam", lastName: "O'Connor", fullName: "Liam O'Connor", email: "liam.oconnor@enterprise.io", phone: "+1 (555) 031-8844", role: "DevOps & SRE Engineer", hireDate: "2026-09-26", salary: 98000.00, status: "ACTIVE", departmentId: 1, departmentName: "Engineering & Cloud Architecture", departmentCode: "ENG", managerId: 6, managerName: "Alexander Wright" }
  ],
  leaves: [
    { id: 1, employeeId: 8, employeeName: "Lucas Bernard", employeeEmail: "lucas.bernard@enterprise.io", departmentName: "Engineering & Cloud Architecture", startDate: "2026-10-19", endDate: "2026-10-26", daysCount: 8, leaveType: "ANNUAL", reason: "Scheduled family holiday and personal leave", status: "APPROVED", appliedAt: "2026-10-10T09:30:00" },
    { id: 2, employeeId: 9, employeeName: "Chloe Dubois", employeeEmail: "chloe.dubois@enterprise.io", departmentName: "Human Resources & People Ops", startDate: "2026-10-24", endDate: "2026-10-25", daysCount: 2, leaveType: "CASUAL", reason: "Attending national human resources symposium", status: "PENDING", appliedAt: "2026-10-20T14:15:00" },
    { id: 3, employeeId: 13, employeeName: "Liam O'Connor", employeeEmail: "liam.oconnor@enterprise.io", departmentName: "Engineering & Cloud Architecture", startDate: "2026-11-01", endDate: "2026-11-03", daysCount: 3, leaveType: "ANNUAL", reason: "Post-onboarding break and personal time", status: "PENDING", appliedAt: "2026-10-20T18:45:00" },
    { id: 4, employeeId: 12, employeeName: "Ryan Gallagher", employeeEmail: "ryan.gallagher@enterprise.io", departmentName: "Global Sales & Growth", startDate: "2026-10-06", endDate: "2026-10-08", daysCount: 3, leaveType: "CASUAL", reason: "Personal urgent commitments", status: "REJECTED", appliedAt: "2026-10-01T11:00:00" },
    { id: 5, employeeId: 7, employeeName: "Maya Patel", employeeEmail: "maya.patel@enterprise.io", departmentName: "Engineering & Cloud Architecture", startDate: "2026-09-20", endDate: "2026-09-22", daysCount: 3, leaveType: "SICK", reason: "Flu recovery and health recuperation", status: "APPROVED", appliedAt: "2026-09-17T08:20:00" }
  ],
  assets: [
    { id: 1, assetTag: "AST-ENG-001", name: "Apple MacBook Pro 16\" M3 Max (64GB)", category: "LAPTOP", serialNumber: "C02GF012MD6T", assignedDate: "2024-06-12", status: "ASSIGNED", assignedEmployeeId: 6, assignedEmployeeName: "Alexander Wright", assignedEmployeeDepartment: "Engineering & Cloud Architecture" },
    { id: 2, assetTag: "AST-ENG-002", name: "Dell UltraSharp 32\" 4K U3223QE", category: "MONITOR", serialNumber: "CN-0M35T1-7444", assignedDate: "2024-06-12", status: "ASSIGNED", assignedEmployeeId: 6, assignedEmployeeName: "Alexander Wright", assignedEmployeeDepartment: "Engineering & Cloud Architecture" },
    { id: 3, assetTag: "AST-ENG-003", name: "Apple MacBook Pro 14\" M3 Pro (36GB)", category: "LAPTOP", serialNumber: "C02HP449KL12", assignedDate: "2024-12-01", status: "ASSIGNED", assignedEmployeeId: 7, assignedEmployeeName: "Maya Patel", assignedEmployeeDepartment: "Engineering & Cloud Architecture" },
    { id: 4, assetTag: "AST-ENG-004", name: "Dell Precision 7680 Workstation", category: "LAPTOP", serialNumber: "8KJG7F3", assignedDate: "2025-07-20", status: "ASSIGNED", assignedEmployeeId: 8, assignedEmployeeName: "Lucas Bernard", assignedEmployeeDepartment: "Engineering & Cloud Architecture" },
    { id: 5, assetTag: "AST-PRD-001", name: "Apple Studio Display 27\" 5K Retina", category: "MONITOR", serialNumber: "F2LF8919N16", assignedDate: "2026-02-15", status: "ASSIGNED", assignedEmployeeId: 11, assignedEmployeeName: "Aisha Al-Mansoor", assignedEmployeeDepartment: "Product Strategy & Design" },
    { id: 6, assetTag: "AST-SEC-001", name: "Corporate Smartcard & Dual-Key RFID Badge", category: "ACCESS_CARD", serialNumber: "AC-88992-V2", assignedDate: "2026-09-26", status: "ASSIGNED", assignedEmployeeId: 13, assignedEmployeeName: "Liam O'Connor", assignedEmployeeDepartment: "Engineering & Cloud Architecture" },
    { id: 7, assetTag: "AST-STK-001", name: "Apple MacBook Pro 16\" M3 Max (36GB)", category: "LAPTOP", serialNumber: "C02KL8819921", assignedDate: null, status: "IN_STOCK", assignedEmployeeId: null, assignedEmployeeName: null, assignedEmployeeDepartment: null },
    { id: 8, assetTag: "AST-STK-002", name: "Dell UltraSharp 27\" USB-C Hub Monitor", category: "MONITOR", serialNumber: "CN-0X984-2199", assignedDate: null, status: "IN_STOCK", assignedEmployeeId: null, assignedEmployeeName: null, assignedEmployeeDepartment: null },
    { id: 9, assetTag: "AST-STK-003", name: "YubiKey 5C NFC Enterprise Token", category: "PERIPHERAL", serialNumber: "YK-9912048-A", assignedDate: null, status: "IN_STOCK", assignedEmployeeId: null, assignedEmployeeName: null, assignedEmployeeDepartment: null },
    { id: 10, assetTag: "AST-REP-001", name: "Lenovo ThinkPad X1 Carbon Gen 12", category: "LAPTOP", serialNumber: "PF49KLL8", assignedDate: null, status: "UNDER_REPAIR", assignedEmployeeId: null, assignedEmployeeName: null, assignedEmployeeDepartment: null }
  ],
  activities: [
    { id: 1, employeeId: 1, employeeName: "Marcus Vance", eventType: "HIRED", description: "Executive appointed as VP of Software Engineering", timestamp: "2022-08-15T09:00:00" },
    { id: 2, employeeId: 6, employeeName: "Alexander Wright", eventType: "HIRED", description: "Joined Engineering team as Senior Cloud Architect", timestamp: "2024-06-12T10:00:00" },
    { id: 3, employeeId: 6, employeeName: "Alexander Wright", eventType: "PROMOTED", description: "Promoted to Principal Cloud Architect; compensation elevated to $172,000", timestamp: "2026-04-15T11:30:00" },
    { id: 4, employeeId: 8, employeeName: "Lucas Bernard", eventType: "HIRED", description: "Joined platform engineering squad", timestamp: "2025-07-20T09:15:00" },
    { id: 5, employeeId: 8, employeeName: "Lucas Bernard", eventType: "STATUS_CHANGE", description: "Transitioned status to ON_LEAVE for approved annual leave", timestamp: "2026-10-19T08:00:00" },
    { id: 6, employeeId: 7, employeeName: "Maya Patel", eventType: "ROLE_CHANGE", description: "Title promoted to Senior Staff Security Engineer", timestamp: "2026-07-01T10:00:00" },
    { id: 7, employeeId: 13, employeeName: "Liam O'Connor", eventType: "HIRED", description: "Joined DevOps & SRE infrastructure squad", timestamp: "2026-09-26T09:00:00" }
  ],
  users: [
    {
      id: 1,
      username: "admin",
      password: "admin123",
      displayName: "Marcus Vance",
      role: "ADMIN",
      roleLabel: "Administrator",
      employeeId: 1,
      email: "marcus.vance@enterprise.io",
      jobTitle: "VP of Software Engineering",
      departmentName: "Engineering & Cloud Architecture",
      permissions: ["EMPLOYEE_WRITE", "SALARY_VIEW", "LEAVE_DECIDE", "LEAVE_VIEW_ALL", "ASSET_WRITE", "ASSET_VIEW_ALL", "ACTIVITY_VIEW", "ORG_ADMIN"]
    },
    {
      id: 2,
      username: "hr",
      password: "hr123",
      displayName: "Elena Rostova",
      role: "HR_MANAGER",
      roleLabel: "HR Manager",
      employeeId: 2,
      email: "elena.rostova@enterprise.io",
      jobTitle: "Director of People & Culture",
      departmentName: "Human Resources & People Ops",
      permissions: ["EMPLOYEE_WRITE", "SALARY_VIEW", "LEAVE_DECIDE", "LEAVE_VIEW_ALL", "ASSET_WRITE", "ASSET_VIEW_ALL", "ACTIVITY_VIEW"]
    },
    {
      id: 3,
      username: "manager",
      password: "manager123",
      displayName: "Alexander Wright",
      role: "MANAGER",
      roleLabel: "Department Manager",
      employeeId: 6,
      email: "alexander.wright@enterprise.io",
      jobTitle: "Principal Cloud Architect",
      departmentName: "Engineering & Cloud Architecture",
      permissions: ["LEAVE_DECIDE", "ASSET_VIEW_ALL", "ACTIVITY_VIEW"]
    },
    {
      id: 4,
      username: "employee",
      password: "emp123",
      displayName: "Liam O'Connor",
      role: "EMPLOYEE",
      roleLabel: "Employee",
      employeeId: 13,
      email: "liam.oconnor@enterprise.io",
      jobTitle: "DevOps & SRE Engineer",
      departmentName: "Engineering & Cloud Architecture",
      permissions: []
    }
  ]
};

const AUTH_STORAGE_KEY = "meridian_auth_user";

function getStoredUser() {
  try {
    const raw = sessionStorage.getItem(AUTH_STORAGE_KEY) || localStorage.getItem(AUTH_STORAGE_KEY);
    return raw ? JSON.parse(raw) : null;
  } catch {
    return null;
  }
}

function setStoredUser(user) {
  try {
    if (user) {
      sessionStorage.setItem(AUTH_STORAGE_KEY, JSON.stringify(user));
      localStorage.setItem(AUTH_STORAGE_KEY, JSON.stringify(user));
    } else {
      sessionStorage.removeItem(AUTH_STORAGE_KEY);
      localStorage.removeItem(AUTH_STORAGE_KEY);
    }
  } catch {
    // ignore storage errors
  }
}

function hasPermission(perm) {
  const user = getStoredUser();
  if (!user) return false;
  if (user.role === "ADMIN") return true;
  return Array.isArray(user.permissions) && user.permissions.includes(perm);
}

window.isOfflineMode = false;

function setConnectionState(isOffline) {
  if (window.isOfflineMode !== isOffline) {
    window.isOfflineMode = isOffline;
    const banner = document.getElementById("offline-banner");
    const dot = document.getElementById("status-dot");
    const statusText = document.getElementById("status-text");

    if (banner) {
      banner.classList.toggle("visible", isOffline);
    }
    if (dot) {
      dot.classList.toggle("offline", isOffline);
    }
    if (statusText) {
      statusText.textContent = isOffline ? "Offline mode" : "All systems operational";
    }
    window.dispatchEvent(new CustomEvent("system:connectionChanged", { detail: { isOffline } }));
  }
}

/**
 * Standard fetch with 4-second timeout and seamless fallback execution.
 */
async function apiRequest(endpoint, options = {}) {
  const controller = new AbortController();
  const timeoutId = setTimeout(() => controller.abort(), 4000);

  try {
    const response = await fetch(endpoint, {
      ...options,
      headers: {
        "Content-Type": "application/json",
        "Accept": "application/json",
        ...(options.headers || {})
      },
      signal: controller.signal
    });

    clearTimeout(timeoutId);

    if (!response.ok) {
      if (response.status === 401 && !endpoint.startsWith("/api/auth/login")) {
        setStoredUser(null);
        if (!window.location.pathname.endsWith("login.html")) {
          window.location.href = "login.html";
        }
      }
      const errorBody = await response.json().catch(() => ({}));
      const error = new Error(errorBody.message || `HTTP Error ${response.status}`);
      error.status = response.status;
      error.validationErrors = errorBody.validationErrors;
      throw error;
    }

    setConnectionState(false);
    if (response.status === 204) return null;
    return await response.json();
  } catch (err) {
    clearTimeout(timeoutId);

    // If network error, timeout, or server unavailable, activate resilient offline fallback
    if (err.name === "AbortError" || err instanceof TypeError || !err.status || err.status >= 500) {
      console.warn(`[EMS API] Network connection failed for ${endpoint}. Activating resilient FALLBACK_STORE.`);
      setConnectionState(true);
      return executeOfflineFallback(endpoint, options);
    }
    throw err;
  }
}

/**
 * Executes fallback simulation operations in-memory when disconnected.
 */
function executeOfflineFallback(endpoint, options) {
  const method = (options.method || "GET").toUpperCase();
  const url = new URL(endpoint, window.location.origin);
  const path = url.pathname;
  const searchParams = url.searchParams;

  // 0. Auth Endpoints in Offline Mode
  if (path === "/api/auth/login" && method === "POST") {
    const body = JSON.parse(options.body || "{}");
    const found = FALLBACK_STORE.users.find(
      u => u.username.toLowerCase() === String(body.username || "").trim().toLowerCase() && u.password === body.password
    );
    if (!found) {
      const err = new Error("Invalid username or password.");
      err.status = 401;
      throw err;
    }
    const { password, ...safeUser } = found;
    setStoredUser(safeUser);
    return safeUser;
  }

  if (path === "/api/auth/logout" && method === "POST") {
    setStoredUser(null);
    return null;
  }

  if (path === "/api/auth/me" && method === "GET") {
    const stored = getStoredUser();
    if (!stored) {
      const err = new Error("Not authenticated");
      err.status = 401;
      throw err;
    }
    return stored;
  }

  if (path === "/api/auth/demo-accounts" && method === "GET") {
    return FALLBACK_STORE.users.map(u => ({
      username: u.username,
      password: u.password,
      displayName: u.displayName,
      role: u.role,
      roleLabel: u.roleLabel,
      jobTitle: u.jobTitle
    }));
  }

  const currentUser = getStoredUser();
  const canViewSalary = !currentUser || hasPermission("SALARY_VIEW");
  const sanitizeEmp = (e) => {
    if (!e || canViewSalary || (currentUser && currentUser.employeeId === e.id)) return e;
    return { ...e, salary: null };
  };
  const requirePerm = (perm) => {
    if (currentUser && !hasPermission(perm)) {
      const err = new Error("You do not have sufficient role permissions to perform this action.");
      err.status = 403;
      throw err;
    }
  };

  // 1. Analytics Summary
  if (path === "/api/analytics/summary") {
    const totalHeadcount = FALLBACK_STORE.employees.length;
    const activeHeadcount = FALLBACK_STORE.employees.filter(e => e.status === "ACTIVE").length;
    const onLeaveHeadcount = FALLBACK_STORE.employees.filter(e => e.status === "ON_LEAVE").length;
    const terminatedHeadcount = FALLBACK_STORE.employees.filter(e => e.status === "TERMINATED").length;
    const pendingLeaves = FALLBACK_STORE.leaves.filter(l => l.status === "PENDING").length;

    const totalAssets = FALLBACK_STORE.assets.length;
    const assignedAssets = FALLBACK_STORE.assets.filter(a => a.status === "ASSIGNED").length;
    const inStockAssets = FALLBACK_STORE.assets.filter(a => a.status === "IN_STOCK").length;
    const underRepairAssets = FALLBACK_STORE.assets.filter(a => a.status === "UNDER_REPAIR").length;

    const departmentDistribution = FALLBACK_STORE.departments.map(dept => {
      const count = FALLBACK_STORE.employees.filter(e => e.departmentId === dept.id).length;
      const pct = totalHeadcount > 0 ? Math.round((count / totalHeadcount) * 1000) / 10 : 0;
      return {
        departmentId: dept.id,
        departmentName: dept.name,
        departmentCode: dept.code,
        employeeCount: count,
        percentage: pct
      };
    });

    return {
      totalHeadcount,
      activeHeadcount,
      onLeaveHeadcount,
      terminatedHeadcount,
      pendingLeavesCount: pendingLeaves,
      totalAssets,
      assignedAssets,
      inStockAssets,
      underRepairAssets,
      recentHiresCount: 2,
      departmentDistribution,
      recentActivities: [...FALLBACK_STORE.activities].reverse().slice(0, 15)
    };
  }

  // 2. Department List
  if (path === "/api/departments") {
    return FALLBACK_STORE.departments.map(d => ({
      ...d,
      employeeCount: FALLBACK_STORE.employees.filter(e => e.departmentId === d.id).length
    }));
  }

  // 3. Employee List (Full Dropdown)
  if (path === "/api/employees/list") {
    return FALLBACK_STORE.employees;
  }

  // 4. Employee Directory & Search
  if (path === "/api/employees" && method === "GET") {
    const search = searchParams.get("search")?.toLowerCase().trim();
    const deptId = searchParams.get("departmentId") ? Number(searchParams.get("departmentId")) : null;
    const status = searchParams.get("status");
    const page = Number(searchParams.get("page") || 0);
    const size = Number(searchParams.get("size") || 10);

    let filtered = FALLBACK_STORE.employees.filter(e => {
      if (search && !e.firstName.toLowerCase().includes(search) &&
          !e.lastName.toLowerCase().includes(search) &&
          !e.email.toLowerCase().includes(search) &&
          !e.role.toLowerCase().includes(search)) {
        return false;
      }
      if (deptId && e.departmentId !== deptId) return false;
      if (status && e.status !== status) return false;
      return true;
    });

    const sortBy = searchParams.get("sortBy") || "id";
    const sortDir = searchParams.get("sortDir") === "desc" ? -1 : 1;
    filtered.sort((a, b) => {
      const x = a[sortBy], y = b[sortBy];
      if (x === y) return 0;
      return (typeof x === "string" ? x.localeCompare(y) : (x > y ? 1 : -1)) * sortDir;
    });

    const start = page * size;
    const paged = filtered.slice(start, start + size).map(sanitizeEmp);

    return {
      content: paged,
      page: {
        size,
        number: page,
        totalElements: filtered.length,
        totalPages: Math.ceil(filtered.length / size)
      }
    };
  }

  // 5. Create Employee
  if (path === "/api/employees" && method === "POST") {
    requirePerm("EMPLOYEE_WRITE");
    const body = JSON.parse(options.body);
    if (FALLBACK_STORE.employees.some(e => e.email.toLowerCase() === String(body.email).toLowerCase())) {
      const dup = new Error(`An employee with email '${body.email}' already exists.`);
      dup.status = 409;
      throw dup;
    }
    const dept = FALLBACK_STORE.departments.find(d => d.id === Number(body.departmentId));
    const mgr = body.managerId ? FALLBACK_STORE.employees.find(e => e.id === Number(body.managerId)) : null;

    const newEmp = {
      id: Date.now(),
      firstName: body.firstName,
      lastName: body.lastName,
      fullName: `${body.firstName} ${body.lastName}`,
      email: body.email,
      phone: body.phone,
      role: body.role,
      hireDate: body.hireDate,
      salary: Number(body.salary),
      status: body.status || "ACTIVE",
      departmentId: dept ? dept.id : null,
      departmentName: dept ? dept.name : "Unassigned",
      departmentCode: dept ? dept.code : "GEN",
      managerId: mgr ? mgr.id : null,
      managerName: mgr ? mgr.fullName : null
    };

    FALLBACK_STORE.employees.unshift(newEmp);
    FALLBACK_STORE.activities.unshift({
      id: Date.now(),
      employeeId: newEmp.id,
      employeeName: newEmp.fullName,
      eventType: "HIRED",
      description: `Joined ${newEmp.departmentName} as ${newEmp.role}`,
      timestamp: new Date().toISOString()
    });

    return newEmp;
  }

  // 6. Update Employee
  if (path.startsWith("/api/employees/") && method === "PUT") {
    const id = Number(path.split("/").pop());
    const body = JSON.parse(options.body);
    const index = FALLBACK_STORE.employees.findIndex(e => e.id === id);
    if (index !== -1) {
      const dept = FALLBACK_STORE.departments.find(d => d.id === Number(body.departmentId));
      const mgr = body.managerId ? FALLBACK_STORE.employees.find(e => e.id === Number(body.managerId)) : null;

      FALLBACK_STORE.employees[index] = {
        ...FALLBACK_STORE.employees[index],
        firstName: body.firstName,
        lastName: body.lastName,
        fullName: `${body.firstName} ${body.lastName}`,
        email: body.email,
        phone: body.phone,
        role: body.role,
        hireDate: body.hireDate,
        salary: Number(body.salary),
        status: body.status,
        departmentId: dept ? dept.id : null,
        departmentName: dept ? dept.name : "Unassigned",
        departmentCode: dept ? dept.code : "GEN",
        managerId: mgr ? mgr.id : null,
        managerName: mgr ? mgr.fullName : null
      };
      return FALLBACK_STORE.employees[index];
    }
  }

  // 6b. Terminate (soft delete) Employee
  if (/^\/api\/employees\/\d+$/.test(path) && method === "DELETE") {
    const id = Number(path.split("/").pop());
    const emp = FALLBACK_STORE.employees.find(e => e.id === id);
    if (emp) {
      FALLBACK_STORE.employees.filter(e => e.managerId === id).forEach(r => {
        r.managerId = emp.managerId;
        r.managerName = emp.managerName;
      });
      emp.status = "TERMINATED";
    }
    return null;
  }

  // 6c. Single employee lookup
  if (/^\/api\/employees\/\d+$/.test(path) && method === "GET") {
    return FALLBACK_STORE.employees.find(e => e.id === Number(path.split("/").pop())) || {};
  }

  // 7. Employee Hierarchy
  if (path.includes("/hierarchy") && method === "GET") {
    const parts = path.split("/");
    const id = Number(parts[parts.length - 2]);
    const emp = FALLBACK_STORE.employees.find(e => e.id === id);
    const mgr = emp && emp.managerId ? FALLBACK_STORE.employees.find(e => e.id === emp.managerId) : null;
    const directReports = FALLBACK_STORE.employees.filter(e => e.managerId === id);

    return {
      employee: emp,
      manager: mgr,
      directReports
    };
  }

  // 8. Leaves List
  if (path === "/api/leaves" && method === "GET") {
    const status = searchParams.get("status");
    const employeeId = searchParams.get("employeeId");
    let result = [...FALLBACK_STORE.leaves];
    if (status) {
      result = result.filter(l => l.status === status);
    }
    if (employeeId) {
      result = result.filter(l => l.employeeId === Number(employeeId));
    }
    return result;
  }

  // 9. Apply Leave
  if (path === "/api/leaves" && method === "POST") {
    const body = JSON.parse(options.body);
    const emp = FALLBACK_STORE.employees.find(e => e.id === Number(body.employeeId));
    const sDate = new Date(body.startDate);
    const eDate = new Date(body.endDate);
    const days = Math.max(1, Math.round((eDate - sDate) / (1000 * 60 * 60 * 24)) + 1);

    const newLeave = {
      id: Date.now(),
      employeeId: emp ? emp.id : null,
      employeeName: emp ? emp.fullName : "Unknown",
      employeeEmail: emp ? emp.email : null,
      departmentName: emp ? emp.departmentName : null,
      startDate: body.startDate,
      endDate: body.endDate,
      daysCount: days,
      leaveType: body.leaveType,
      reason: body.reason,
      status: "PENDING",
      appliedAt: new Date().toISOString()
    };
    FALLBACK_STORE.leaves.unshift(newLeave);
    return newLeave;
  }

  // 10. Approve Leave
  if (path.includes("/approve") && method === "PATCH") {
    const id = Number(path.split("/")[3]);
    const leave = FALLBACK_STORE.leaves.find(l => l.id === id);
    if (leave) {
      leave.status = "APPROVED";
      const emp = FALLBACK_STORE.employees.find(e => e.id === leave.employeeId);
      if (emp) {
        emp.status = "ON_LEAVE";
      }
    }
    return leave;
  }

  // 11. Reject Leave
  if (path.includes("/reject") && method === "PATCH") {
    const id = Number(path.split("/")[3]);
    const leave = FALLBACK_STORE.leaves.find(l => l.id === id);
    if (leave) {
      leave.status = "REJECTED";
    }
    return leave;
  }

  // 12. Assets List
  if (path === "/api/assets" && method === "GET") {
    const status = searchParams.get("status");
    const category = searchParams.get("category");
    return FALLBACK_STORE.assets.filter(a => {
      if (status && a.status !== status) return false;
      if (category && a.category !== category) return false;
      return true;
    });
  }

  // 13. Create Asset
  if (path === "/api/assets" && method === "POST") {
    const body = JSON.parse(options.body);
    const emp = body.assignedEmployeeId ? FALLBACK_STORE.employees.find(e => e.id === Number(body.assignedEmployeeId)) : null;

    const newAsset = {
      id: Date.now(),
      assetTag: body.assetTag,
      name: body.name,
      category: body.category,
      serialNumber: body.serialNumber,
      assignedDate: emp ? new Date().toISOString().split("T")[0] : null,
      status: emp ? "ASSIGNED" : body.status || "IN_STOCK",
      assignedEmployeeId: emp ? emp.id : null,
      assignedEmployeeName: emp ? emp.fullName : null,
      assignedEmployeeDepartment: emp ? emp.departmentName : null
    };

    FALLBACK_STORE.assets.unshift(newAsset);
    return newAsset;
  }

  // 14. Checkout Asset
  if (path.endsWith("/checkout") && method === "POST") {
    const id = Number(path.split("/")[3]);
    const body = JSON.parse(options.body);
    const asset = FALLBACK_STORE.assets.find(a => a.id === id);
    const emp = FALLBACK_STORE.employees.find(e => e.id === Number(body.employeeId));

    if (asset && emp) {
      asset.assignedEmployeeId = emp.id;
      asset.assignedEmployeeName = emp.fullName;
      asset.assignedEmployeeDepartment = emp.departmentName;
      asset.assignedDate = new Date().toISOString().split("T")[0];
      asset.status = "ASSIGNED";
    }
    return asset;
  }

  // 15. Return Asset
  if (path.endsWith("/return") && method === "POST") {
    const id = Number(path.split("/")[3]);
    const asset = FALLBACK_STORE.assets.find(a => a.id === id);
    if (asset) {
      asset.assignedEmployeeId = null;
      asset.assignedEmployeeName = null;
      asset.assignedEmployeeDepartment = null;
      asset.assignedDate = null;
      asset.status = "IN_STOCK";
    }
    return asset;
  }

  // 16. Send Asset to Repair
  if (path.endsWith("/repair") && method === "POST") {
    const id = Number(path.split("/")[3]);
    const asset = FALLBACK_STORE.assets.find(a => a.id === id);
    if (asset) {
      asset.assignedEmployeeId = null;
      asset.assignedEmployeeName = null;
      asset.assignedEmployeeDepartment = null;
      asset.assignedDate = null;
      asset.status = "UNDER_REPAIR";
    }
    return asset;
  }

  return {};
}

  // Global API Helper Module
window.EMS_API = {
  login: async (username, password) => {
    const user = await apiRequest("/api/auth/login", {
      method: "POST",
      body: JSON.stringify({ username, password })
    });
    setStoredUser(user);
    return user;
  },
  logout: async () => {
    try {
      await apiRequest("/api/auth/logout", { method: "POST" });
    } finally {
      setStoredUser(null);
    }
  },
  getCurrentUser: async () => {
    try {
      const user = await apiRequest("/api/auth/me");
      if (user) setStoredUser(user);
      return user;
    } catch (err) {
      if (err.status === 401) {
        setStoredUser(null);
        return null;
      }
      return getStoredUser();
    }
  },
  getDemoAccounts: () => apiRequest("/api/auth/demo-accounts"),
  getStoredUser,
  hasPermission,
  getAnalyticsSummary: () => apiRequest("/api/analytics/summary"),
  getDepartments: () => apiRequest("/api/departments"),
  getEmployees: (params) => {
    const query = new URLSearchParams(params).toString();
    return apiRequest(`/api/employees?${query}`);
  },
  getAllEmployeesList: () => apiRequest("/api/employees/list"),
  getEmployeeById: (id) => apiRequest(`/api/employees/${id}`),
  getEmployeeHierarchy: (id) => apiRequest(`/api/employees/${id}/hierarchy`),
  createEmployee: (data) => apiRequest("/api/employees", { method: "POST", body: JSON.stringify(data) }),
  updateEmployee: (id, data) => apiRequest(`/api/employees/${id}`, { method: "PUT", body: JSON.stringify(data) }),
  deleteEmployee: (id) => apiRequest(`/api/employees/${id}`, { method: "DELETE" }),
  getLeaves: (params) => {
    const query = new URLSearchParams(params).toString();
    return apiRequest(`/api/leaves?${query}`);
  },
  applyLeave: (data) => apiRequest("/api/leaves", { method: "POST", body: JSON.stringify(data) }),
  approveLeave: (id, remarks) => apiRequest(`/api/leaves/${id}/approve`, { method: "PATCH", body: JSON.stringify({ remarks }) }),
  rejectLeave: (id, remarks) => apiRequest(`/api/leaves/${id}/reject`, { method: "PATCH", body: JSON.stringify({ remarks }) }),
  getAssets: (params) => {
    const query = new URLSearchParams(params).toString();
    return apiRequest(`/api/assets?${query}`);
  },
  createAsset: (data) => apiRequest("/api/assets", { method: "POST", body: JSON.stringify(data) }),
  checkoutAsset: (assetId, employeeId) => apiRequest(`/api/assets/${assetId}/checkout`, { method: "POST", body: JSON.stringify({ employeeId }) }),
  returnAsset: (assetId) => apiRequest(`/api/assets/${assetId}/return`, { method: "POST" }),
  repairAsset: (assetId) => apiRequest(`/api/assets/${assetId}/repair`, { method: "POST" })
};
