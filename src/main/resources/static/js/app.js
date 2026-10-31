/**
 * Enterprise Employee Management System (EMS) - UI Engine
 * Pure Vanilla ES6+, zero external dependencies.
 */

// ==============================================================================
// Icons (pure inline SVG, no emojis, no icon fonts)
// ==============================================================================
const ICONS = {
  users: `<svg viewBox="0 0 24 24"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 0 0-3-3.87"/><path d="M16 3.13a4 4 0 0 1 0 7.75"/></svg>`,
  calendar: `<svg viewBox="0 0 24 24"><rect x="3" y="4" width="18" height="18" rx="2" ry="2"/><line x1="16" y1="2" x2="16" y2="6"/><line x1="8" y1="2" x2="8" y2="6"/><line x1="3" y1="10" x2="21" y2="10"/></svg>`,
  laptop: `<svg viewBox="0 0 24 24"><rect x="2" y="3" width="20" height="14" rx="2" ry="2"/><line x1="2" y1="20" x2="22" y2="20"/></svg>`,
  plus: `<svg viewBox="0 0 24 24"><line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/></svg>`,
  edit: `<svg viewBox="0 0 24 24"><path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/><path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/></svg>`,
  check: `<svg viewBox="0 0 24 24"><polyline points="20 6 9 17 4 12"/></svg>`,
  cross: `<svg viewBox="0 0 24 24"><line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/></svg>`,
  tree: `<svg viewBox="0 0 24 24"><circle cx="12" cy="5" r="3"/><circle cx="5" cy="19" r="3"/><circle cx="19" cy="19" r="3"/><line x1="12" y1="8" x2="12" y2="12"/><path d="M5 16v-4h14v4"/></svg>`,
  refresh: `<svg viewBox="0 0 24 24"><polyline points="23 4 23 10 17 10"/><polyline points="1 20 1 14 7 14"/><path d="M3.51 9a9 9 0 0 1 14.85-3.36L23 10M1 14l4.64 4.36A9 9 0 0 0 20.49 15"/></svg>`,
  wrench: `<svg viewBox="0 0 24 24"><path d="M14.7 6.3a1 1 0 0 0 0 1.4l1.6 1.6a1 1 0 0 0 1.4 0l3.77-3.77a6 6 0 0 1-7.94 7.94l-6.91 6.91a2.12 2.12 0 0 1-3-3l6.91-6.91a6 6 0 0 1 7.94-7.94l-3.76 3.76z"/></svg>`,
  clock: `<svg viewBox="0 0 24 24"><circle cx="12" cy="12" r="10"/><polyline points="12 6 12 12 16 14"/></svg>`,
  alert: `<svg viewBox="0 0 24 24"><circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="12"/><line x1="12" y1="16" x2="12.01" y2="16"/></svg>`,
  dashboard: `<svg viewBox="0 0 24 24"><rect x="3" y="3" width="7" height="9" rx="1"/><rect x="14" y="3" width="7" height="5" rx="1"/><rect x="14" y="12" width="7" height="9" rx="1"/><rect x="3" y="16" width="7" height="5" rx="1"/></svg>`,
  search: `<svg viewBox="0 0 24 24"><circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/></svg>`,
  bell: `<svg viewBox="0 0 24 24"><path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9"/><path d="M13.73 21a2 2 0 0 1-3.46 0"/></svg>`,
  database: `<svg viewBox="0 0 24 24"><ellipse cx="12" cy="5" rx="9" ry="3"/><path d="M21 12c0 1.66-4 3-9 3s-9-1.34-9-3"/><path d="M3 5v14c0 1.66 4 3 9 3s9-1.34 9-3V5"/></svg>`,
  lock: `<svg viewBox="0 0 24 24"><rect x="3" y="11" width="18" height="11" rx="2"/><path d="M7 11V7a5 5 0 0 1 10 0v4"/></svg>`,
  logout: `<svg viewBox="0 0 24 24"><path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/><polyline points="16 17 21 12 16 7"/><line x1="21" y1="12" x2="9" y2="12"/></svg>`
};

const BRAND = { name: "Meridian", tagline: "People Suite" };
const LOGO_SVG = `<svg class="brand-logo" viewBox="0 0 32 32" aria-hidden="true"><rect width="32" height="32" rx="8" fill="#3256d6"/><path d="M8.5 22.5V10.5L16 18l7.5-7.5v12" fill="none" stroke="#fff" stroke-width="2.8" stroke-linecap="round" stroke-linejoin="round"/></svg>`;

/** Deterministic avatar colour derived from a name, so each person keeps a stable colour. */
function avatarColor(name) {
  let h = 0;
  for (const ch of String(name || "")) h = (h * 31 + ch.charCodeAt(0)) % 360;
  return `hsl(${h}, 52%, 46%)`;
}

// ==============================================================================
// Utilities
// ==============================================================================
const currencyFormatter = new Intl.NumberFormat("en-US", { style: "currency", currency: "USD", maximumFractionDigits: 0 });
const dateFormatter = new Intl.DateTimeFormat("en-US", { year: "numeric", month: "short", day: "numeric" });

/** Escapes untrusted text before it is interpolated into HTML (XSS protection). */
function esc(value) {
  if (value === null || value === undefined) return "";
  return String(value)
    .replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;")
    .replace(/"/g, "&quot;").replace(/'/g, "&#39;");
}

const formatCurrency = (val) => (val === null || val === undefined) ? "$0" : currencyFormatter.format(val);

function formatDate(val) {
  if (!val) return "-";
  const d = new Date(val);
  return isNaN(d) ? esc(val) : dateFormatter.format(d);
}

function formatRelativeTime(iso) {
  if (!iso) return "";
  const diff = Math.round((Date.now() - new Date(iso)) / 1000);
  if (diff < 60) return "Just now";
  if (diff < 3600) return `${Math.floor(diff / 60)}m ago`;
  if (diff < 86400) return `${Math.floor(diff / 3600)}h ago`;
  if (diff < 604800) return `${Math.floor(diff / 86400)}d ago`;
  return formatDate(iso);
}

function debounce(fn, wait) {
  let t;
  return (...args) => { clearTimeout(t); t = setTimeout(() => fn(...args), wait); };
}

const $ = (id) => document.getElementById(id);
const setText = (id, v) => { const el = $(id); if (el) el.textContent = v; };

function skeletonRows(cols, rows = 5) {
  const cell = `<td><span class="skeleton"></span></td>`;
  return Array.from({ length: rows }, () => `<tr>${cell.repeat(cols)}</tr>`).join("");
}

function emptyRow(cols, icon, title, hint = "") {
  return `<tr><td colspan="${cols}"><div class="empty-state">${icon}<h4>${esc(title)}</h4>${hint ? `<p>${esc(hint)}</p>` : ""}</div></td></tr>`;
}

function errorRow(cols, message) {
  return `<tr><td colspan="${cols}" style="color:var(--danger); text-align:center; padding:2rem;">${esc(message)}</td></tr>`;
}

function dayCount(start, end) {
  return Math.max(1, Math.round((new Date(end) - new Date(start)) / 86400000) + 1);
}

// ==============================================================================
// Toasts, Modals, Confirm dialog
// ==============================================================================
function showToast(message, type = "info", duration = 3500) {
  const container = $("toast-container");
  if (!container) return;
  container.setAttribute("role", "status");
  container.setAttribute("aria-live", "polite");

  const icon = { success: ICONS.check, error: ICONS.cross, warning: ICONS.alert, info: ICONS.clock }[type] || ICONS.clock;
  const toast = document.createElement("div");
  toast.className = `toast toast-${type}`;
  toast.innerHTML = `${icon}<span>${esc(message)}</span>`;
  container.appendChild(toast);

  setTimeout(() => {
    toast.style.opacity = "0";
    toast.style.transition = "opacity 0.2s ease";
    setTimeout(() => toast.remove(), 250);
  }, duration);
}

let lastFocused = null;

function openModal(id) {
  const modal = $(id);
  if (!modal) return;
  lastFocused = document.activeElement;
  modal.classList.add("active");
  modal.setAttribute("role", "dialog");
  modal.setAttribute("aria-modal", "true");
  document.body.style.overflow = "hidden";
  const first = modal.querySelector("input:not([type=hidden]), select, textarea, button.btn-primary");
  if (first) setTimeout(() => first.focus(), 60);
}

function closeModal(id) {
  const modal = $(id);
  if (!modal) return;
  modal.classList.remove("active");
  if (!document.querySelector(".modal-overlay.active")) document.body.style.overflow = "";
  if (lastFocused && lastFocused.focus) lastFocused.focus();
}

document.addEventListener("keydown", (e) => {
  if (e.key === "Escape") {
    document.querySelectorAll(".modal-overlay.active").forEach(m => closeModal(m.id));
  }
  // Keep keyboard focus trapped inside the open modal
  if (e.key === "Tab") {
    const modal = document.querySelector(".modal-overlay.active");
    if (!modal) return;
    const items = [...modal.querySelectorAll("button, input:not([type=hidden]), select, textarea, a[href]")].filter(el => !el.disabled);
    if (!items.length) return;
    const first = items[0], last = items[items.length - 1];
    if (e.shiftKey && document.activeElement === first) { e.preventDefault(); last.focus(); }
    else if (!e.shiftKey && document.activeElement === last) { e.preventDefault(); first.focus(); }
  }
});

document.addEventListener("click", (e) => {
  if (e.target.classList && e.target.classList.contains("modal-overlay")) closeModal(e.target.id);
});

/** Promise-based confirmation dialog replacing window.confirm. */
function confirmDialog({ title, message, confirmLabel = "Confirm", danger = false }) {
  return new Promise((resolve) => {
    const overlay = document.createElement("div");
    overlay.className = "modal-overlay";
    overlay.id = "confirm-overlay";
    overlay.innerHTML = `
      <div class="modal-dialog confirm-dialog">
        <div class="modal-header"><h3>${esc(title)}</h3></div>
        <div class="modal-body"><p class="confirm-message">${esc(message)}</p></div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" data-act="cancel">Cancel</button>
          <button type="button" class="btn ${danger ? "btn-danger" : "btn-primary"}" data-act="ok">${esc(confirmLabel)}</button>
        </div>
      </div>`;
    document.body.appendChild(overlay);
    requestAnimationFrame(() => overlay.classList.add("active"));
    document.body.style.overflow = "hidden";
    const previous = document.activeElement;

    const finish = (result) => {
      overlay.classList.remove("active");
      document.removeEventListener("keydown", onKey, true);
      if (!document.querySelector(".modal-overlay.active:not(#confirm-overlay)")) document.body.style.overflow = "";
      setTimeout(() => overlay.remove(), 200);
      if (previous && previous.focus) previous.focus();
      resolve(result);
    };
    const onKey = (e) => {
      if (e.key === "Escape") { e.stopPropagation(); finish(false); }
    };
    document.addEventListener("keydown", onKey, true);
    overlay.addEventListener("click", (e) => {
      if (e.target === overlay) finish(false);
      const act = e.target.closest("[data-act]");
      if (act) finish(act.dataset.act === "ok");
    });
    overlay.querySelector('[data-act="ok"]').focus();
  });
}

// ==============================================================================
// Form helpers: inline validation
// ==============================================================================
function clearFormErrors(form) {
  form.querySelectorAll(".field-error").forEach(el => el.remove());
  form.querySelectorAll(".invalid").forEach(el => { el.classList.remove("invalid"); el.removeAttribute("aria-invalid"); });
}

function showFieldError(inputId, message) {
  const input = $(inputId);
  if (!input) return;
  input.classList.add("invalid");
  input.setAttribute("aria-invalid", "true");
  const note = document.createElement("small");
  note.className = "field-error";
  note.textContent = message;
  input.parentElement.appendChild(note);
}

/** Maps server validationErrors (by field name) to inline messages; returns true if any shown. */
function applyServerErrors(form, fieldMap, err) {
  clearFormErrors(form);
  if (!err.validationErrors) return false;
  let shown = false;
  Object.entries(err.validationErrors).forEach(([field, msg]) => {
    if (fieldMap[field]) { showFieldError(fieldMap[field], msg); shown = true; }
  });
  return shown;
}

function renderStatusBadge(status) {
  if (!status) return "-";
  const st = String(status).toUpperCase();
  let cls = "badge-info";
  if (["ACTIVE", "APPROVED", "IN_STOCK"].includes(st)) cls = "badge-active";
  else if (["ON_LEAVE", "PENDING", "UNDER_REPAIR"].includes(st)) cls = "badge-on-leave";
  else if (["TERMINATED", "REJECTED"].includes(st)) cls = "badge-terminated";
  return `<span class="badge ${cls}">${esc(st.replace(/_/g, " "))}</span>`;
}

/** Wires up sortable <th data-sort="field"> headers; calls onChange(sortBy, sortDir). */
function bindSortableHeaders(table, state, onChange) {
  table.querySelectorAll("th[data-sort]").forEach(th => {
    th.tabIndex = 0;
    const activate = () => {
      const field = th.dataset.sort;
      state.sortDir = state.sortBy === field && state.sortDir === "asc" ? "desc" : "asc";
      state.sortBy = field;
      table.querySelectorAll("th[data-sort]").forEach(h => h.removeAttribute("aria-sort"));
      th.setAttribute("aria-sort", state.sortDir === "asc" ? "ascending" : "descending");
      onChange();
    };
    th.addEventListener("click", activate);
    th.addEventListener("keydown", (e) => { if (e.key === "Enter" || e.key === " ") { e.preventDefault(); activate(); } });
  });
}

// ==============================================================================
// 1. DASHBOARD
// ==============================================================================
async function initDashboardPage() {
  setText("today-date", new Intl.DateTimeFormat("en-US", { weekday: "long", month: "long", day: "numeric", year: "numeric" }).format(new Date()));

  try {
    const s = await window.EMS_API.getAnalyticsSummary();
    if (!s) return;

    const kpi = $("kpi-container");
    if (kpi) {
      const cards = [
        ["--primary", "Total headcount", ICONS.users, s.totalHeadcount, `${s.recentHiresCount} joined in the last 90 days`],
        ["--success", "Active employees", ICONS.check, s.activeHeadcount, `${s.onLeaveHeadcount} currently on leave`],
        ["--warning", "Pending time off", ICONS.calendar, s.pendingLeavesCount, "Awaiting your approval"],
        ["--info", "Company assets", ICONS.laptop, s.totalAssets, `${s.assignedAssets} assigned, ${s.inStockAssets} in stock, ${s.underRepairAssets} in repair`]
      ];
      kpi.innerHTML = cards.map(([accent, label, icon, value, sub]) => `
        <div class="kpi-card" style="--kpi-accent: var(${accent})">
          <div class="kpi-header"><span class="kpi-label">${label}</span><div class="kpi-icon-wrap">${icon}</div></div>
          <div class="kpi-value">${esc(value)}</div>
          <div class="kpi-subtext">${esc(sub)}</div>
        </div>`).join("");
    }

    // Workforce status donut (pure CSS conic-gradient)
    const donut = $("status-breakdown");
    if (donut) {
      const total = Math.max(1, s.totalHeadcount);
      const parts = [
        ["Active", s.activeHeadcount, "var(--success)"],
        ["On leave", s.onLeaveHeadcount, "var(--warning)"],
        ["Terminated", s.terminatedHeadcount, "var(--danger)"]
      ];
      let acc = 0;
      const stops = parts.filter(p => p[1] > 0).map(([, n, color]) => {
        const start = acc; acc += (n / total) * 100;
        return `${color} ${start}% ${acc}%`;
      });
      donut.innerHTML = `
        <div class="donut-wrap">
          <div class="donut" style="background: conic-gradient(${stops.join(", ") || "var(--bg-hover) 0 100%"})" role="img" aria-label="Workforce status breakdown">
            <div class="donut-center"><strong>${s.totalHeadcount}</strong><span>People</span></div>
          </div>
          <div class="legend">
            ${parts.map(([label, n, color]) => `<div class="legend-row"><span class="legend-swatch" style="background:${color}"></span>${label}<b>${n}</b></div>`).join("")}
          </div>
        </div>`;
    }

    const dist = $("department-distribution");
    if (dist) {
      const max = Math.max(1, ...(s.departmentDistribution || []).map(d => d.percentage));
      dist.innerHTML = (s.departmentDistribution || []).map(d => `
        <div class="dist-item">
          <div class="dist-item-header">
            <span class="dist-name">${esc(d.departmentName)}</span>
            <span class="dist-stats">${d.employeeCount} people &middot; ${d.percentage}%</span>
          </div>
          <div class="dist-track" role="progressbar" aria-valuenow="${d.percentage}" aria-valuemin="0" aria-valuemax="100" aria-label="${esc(d.departmentName)} share">
            <div class="dist-fill" style="width:${(d.percentage / max) * 100}%"></div>
          </div>
        </div>`).join("");
    }

    const tl = $("activity-timeline");
    if (tl) {
      const acts = s.recentActivities || [];
      const nodeClass = { HIRED: "node-hired", PROMOTED: "node-promoted", TRANSFERRED: "node-transferred", TERMINATED: "node-terminated" };
      tl.innerHTML = acts.length === 0
        ? `<div class="empty-state">${ICONS.clock}<h4>No recent activity</h4></div>`
        : acts.map(a => `
          <div class="timeline-item">
            <div class="timeline-node ${nodeClass[a.eventType] || ""}"></div>
            <div class="timeline-content">
              <div class="timeline-meta">
                <span class="timeline-title">${esc(a.employeeName)}</span>
                <span class="timeline-time">${formatRelativeTime(a.timestamp)}</span>
              </div>
              <div class="timeline-desc">${esc(a.description)}</div>
            </div>
          </div>`).join("");
    }
  } catch (err) {
    showToast("Failed to load dashboard: " + err.message, "error");
  }

  // Pending approvals widget
  const pending = $("pending-list");
  if (pending) {
    try {
      const leaves = (await window.EMS_API.getLeaves({ status: "PENDING" })).slice(0, 5);
      pending.innerHTML = leaves.length === 0
        ? `<div class="empty-state" style="padding:1.5rem;">${ICONS.check}<h4>You're all caught up</h4></div>`
        : leaves.map(l => `
          <div class="list-row">
            <div class="avatar-circle" style="background:${avatarColor(l.employeeName)}" aria-hidden="true">${esc(l.employeeName.split(" ").map(w => w[0]).slice(0, 2).join(""))}</div>
            <div class="list-row-main"><strong>${esc(l.employeeName)}</strong><span>${esc(l.leaveType)} &middot; ${formatDate(l.startDate)} to ${formatDate(l.endDate)}</span></div>
            <a class="btn btn-secondary btn-sm" href="leave.html">Review</a>
          </div>`).join("");
    } catch (err) {
      pending.innerHTML = `<div class="field-error">${esc(err.message)}</div>`;
    }
  }
}

// ==============================================================================
// 2. EMPLOYEES
// ==============================================================================
const empState = { page: 0, size: 10, sortBy: "id", sortDir: "asc", totalPages: 1 };
const EMP_FIELD_MAP = {
  firstName: "emp-form-fname", lastName: "emp-form-lname", email: "emp-form-email", phone: "emp-form-phone",
  role: "emp-form-role", hireDate: "emp-form-hiredate", salary: "emp-form-salary",
  status: "emp-form-status", departmentId: "emp-form-dept"
};

async function loadEmployees() {
  const body = $("employee-table-body");
  if (!body) return;
  body.innerHTML = skeletonRows(7);

  const params = { page: empState.page, size: empState.size, sortBy: empState.sortBy, sortDir: empState.sortDir };
  const q = $("employee-search")?.value.trim();
  if (q) params.search = q;
  if ($("employee-dept-filter")?.value) params.departmentId = $("employee-dept-filter").value;
  if ($("employee-status-filter")?.value) params.status = $("employee-status-filter").value;

  try {
    const data = await window.EMS_API.getEmployees(params);
    const list = data.content || [];
    const meta = data.page || { totalElements: list.length, totalPages: 1 };
    empState.totalPages = Math.max(1, meta.totalPages);

    // Page index went out of range (e.g. after filtering): step back
    if (empState.page > 0 && list.length === 0 && meta.totalElements > 0) {
      empState.page = empState.totalPages - 1;
      return loadEmployees();
    }

    setText("total-emp-count", meta.totalElements);
    setText("page-info", `Page ${empState.page + 1} of ${empState.totalPages}`);
    if ($("prev-page-btn")) $("prev-page-btn").disabled = empState.page <= 0;
    if ($("next-page-btn")) $("next-page-btn").disabled = empState.page >= empState.totalPages - 1;

    if (list.length === 0) {
      body.innerHTML = emptyRow(7, ICONS.users, "No employees found", "Try adjusting your search or filters");
      return;
    }

    const canWriteEmp = window.EMS_API.hasPermission("EMPLOYEE_WRITE");

    body.innerHTML = list.map(emp => `
      <tr>
        <td>
          <div class="user-cell">
            <div class="avatar-circle" style="background:${avatarColor(emp.fullName)}" aria-hidden="true">${esc((emp.firstName[0] || "") + (emp.lastName[0] || ""))}</div>
            <div><div class="user-name">${esc(emp.fullName)}</div><div class="user-email">${esc(emp.email)}</div></div>
          </div>
        </td>
        <td><div style="font-weight:500;">${esc(emp.role)}</div><div class="font-mono">${esc(emp.phone)}</div></td>
        <td><div style="font-weight:600;">${esc(emp.departmentName || "-")}</div><span class="badge badge-info">${esc(emp.departmentCode || "GEN")}</span></td>
        <td>${emp.managerName ? esc(emp.managerName) : `<span style="color:var(--text-dim)">Leadership</span>`}</td>
        <td>
          ${emp.salary !== null && emp.salary !== undefined
            ? `<div style="font-weight:600;">${formatCurrency(emp.salary)}</div>`
            : `<span class="salary-redacted" title="Compensation visible only to HR &amp; Administrators">${ICONS.lock} Restricted</span>`}
          <div class="font-mono">Hired ${formatDate(emp.hireDate)}</div>
        </td>
        <td>${renderStatusBadge(emp.status)}</td>
        <td style="text-align:right;">
          <div class="table-row-actions">
            <button class="btn btn-secondary btn-sm" data-action="tree" data-id="${emp.id}" aria-label="View hierarchy for ${esc(emp.fullName)}">${ICONS.tree} Tree</button>
            ${canWriteEmp ? `<button class="btn btn-secondary btn-sm btn-icon-only" data-action="edit" data-id="${emp.id}" aria-label="Edit ${esc(emp.fullName)}" title="Edit">${ICONS.edit}</button>` : ""}
            ${canWriteEmp && emp.status !== "TERMINATED" ? `<button class="btn btn-danger btn-sm btn-icon-only" data-action="terminate" data-id="${emp.id}" data-name="${esc(emp.fullName)}" aria-label="Terminate ${esc(emp.fullName)}" title="Terminate">${ICONS.cross}</button>` : ""}
          </div>
        </td>
      </tr>`).join("");
  } catch (err) {
    body.innerHTML = errorRow(7, "Error: " + err.message);
    showToast("Failed to load employees: " + err.message, "error");
  }
}

async function loadManagerDropdown() {
  try {
    const managers = await window.EMS_API.getAllEmployeesList();
    const sel = $("emp-form-manager");
    if (sel) {
      sel.innerHTML = `<option value="">None (Top Executive)</option>` +
        managers.filter(m => m.status !== "TERMINATED")
          .map(m => `<option value="${m.id}">${esc(m.fullName)} - ${esc(m.role)} (${esc(m.departmentCode)})</option>`).join("");
    }
  } catch (e) {
    console.error("Error loading managers", e);
  }
}

async function initEmployeesPage() {
  const presetQuery = new URLSearchParams(window.location.search).get("q");
  if (presetQuery && $("employee-search")) $("employee-search").value = presetQuery;

  try {
    const depts = await window.EMS_API.getDepartments();
    const options = depts.map(d => `<option value="${d.id}">${esc(d.name)} (${esc(d.code)})</option>`).join("");
    if ($("employee-dept-filter")) $("employee-dept-filter").innerHTML = `<option value="">All Departments</option>` + options;
    if ($("emp-form-dept")) $("emp-form-dept").innerHTML = `<option value="">Select Department</option>` + options;
  } catch (e) {
    console.error("Error loading departments", e);
  }
  loadManagerDropdown();

  const reset = () => { empState.page = 0; loadEmployees(); };
  $("employee-search")?.addEventListener("input", debounce(reset, 300));
  $("employee-dept-filter")?.addEventListener("change", reset);
  $("employee-status-filter")?.addEventListener("change", reset);
  $("prev-page-btn")?.addEventListener("click", () => { if (empState.page > 0) { empState.page--; loadEmployees(); } });
  $("next-page-btn")?.addEventListener("click", () => { if (empState.page < empState.totalPages - 1) { empState.page++; loadEmployees(); } });

  const table = document.querySelector("#employee-table-body")?.closest("table");
  if (table) bindSortableHeaders(table, empState, () => { empState.page = 0; loadEmployees(); });

  // Single delegated listener for row actions (no inline onclick, no duplicates)
  $("employee-table-body")?.addEventListener("click", (e) => {
    const btn = e.target.closest("button[data-action]");
    if (!btn) return;
    const id = Number(btn.dataset.id);
    if (btn.dataset.action === "tree") viewHierarchy(id);
    else if (btn.dataset.action === "edit") openEditEmployeeModal(id);
    else if (btn.dataset.action === "terminate") terminateEmployee(id, btn.dataset.name);
  });

  $("employee-form")?.addEventListener("submit", async (e) => {
    e.preventDefault();
    const form = e.currentTarget;
    clearFormErrors(form);
    const id = $("emp-form-id").value;
    const payload = {
      firstName: $("emp-form-fname").value.trim(),
      lastName: $("emp-form-lname").value.trim(),
      email: $("emp-form-email").value.trim(),
      phone: $("emp-form-phone").value.trim(),
      role: $("emp-form-role").value.trim(),
      hireDate: $("emp-form-hiredate").value,
      salary: parseFloat($("emp-form-salary").value),
      status: $("emp-form-status").value,
      departmentId: parseInt($("emp-form-dept").value, 10),
      managerId: $("emp-form-manager").value ? parseInt($("emp-form-manager").value, 10) : null
    };

    const submit = form.querySelector('button[type="submit"]');
    submit.disabled = true;
    try {
      if (id) await window.EMS_API.updateEmployee(id, payload);
      else await window.EMS_API.createEmployee(payload);
      showToast(`${id ? "Updated" : "Created"} ${payload.firstName} ${payload.lastName}`, "success");
      closeModal("employee-modal");
      loadEmployees();
      loadManagerDropdown();
    } catch (err) {
      if (!applyServerErrors(form, EMP_FIELD_MAP, err)) {
        if (err.status === 409) showFieldError("emp-form-email", err.message);
        else showToast(err.message, "error");
      }
    } finally {
      submit.disabled = false;
    }
  });

  loadEmployees();
}

function openCreateEmployeeModal() {
  const form = $("employee-form");
  form.reset();
  clearFormErrors(form);
  $("emp-form-id").value = "";
  $("emp-modal-title").textContent = "Add New Employee";
  $("emp-form-hiredate").value = new Date().toISOString().split("T")[0];
  $("emp-form-hiredate").max = new Date().toISOString().split("T")[0];
  openModal("employee-modal");
}

async function openEditEmployeeModal(id) {
  try {
    const emp = await window.EMS_API.getEmployeeById(id);
    const form = $("employee-form");
    form.reset();
    clearFormErrors(form);
    $("emp-form-id").value = emp.id;
    $("emp-form-fname").value = emp.firstName;
    $("emp-form-lname").value = emp.lastName;
    $("emp-form-email").value = emp.email;
    $("emp-form-phone").value = emp.phone;
    $("emp-form-role").value = emp.role;
    $("emp-form-hiredate").value = emp.hireDate;
    $("emp-form-hiredate").max = new Date().toISOString().split("T")[0];
    $("emp-form-salary").value = emp.salary;
    $("emp-form-status").value = emp.status;
    $("emp-form-dept").value = emp.departmentId;
    $("emp-form-manager").value = emp.managerId || "";
    $("emp-modal-title").textContent = `Edit Employee #${emp.id}`;
    openModal("employee-modal");
  } catch (err) {
    showToast("Failed to fetch employee: " + err.message, "error");
  }
}

async function terminateEmployee(id, name) {
  const ok = await confirmDialog({
    title: "Terminate employee",
    message: `Terminate ${name}? Their direct reports will be reassigned to the next manager up the hierarchy.`,
    confirmLabel: "Terminate",
    danger: true
  });
  if (!ok) return;
  try {
    await window.EMS_API.deleteEmployee(id);
    showToast(`Terminated ${name}`, "warning");
    loadEmployees();
    loadManagerDropdown();
  } catch (err) {
    showToast("Termination failed: " + err.message, "error");
  }
}

async function viewHierarchy(id) {
  try {
    const data = await window.EMS_API.getEmployeeHierarchy(id);
    const content = $("hierarchy-content");
    if (!content) return;
    const e = data.employee;
    const mgr = data.manager
      ? `<div class="tree-block"><div class="tree-label">Reports to</div><div class="tree-name">${esc(data.manager.fullName)}</div><div class="tree-sub">${esc(data.manager.role)} &bull; ${esc(data.manager.departmentName)}</div></div>`
      : `<div class="tree-sub" style="margin-bottom:1rem;">Reports directly to the executive board</div>`;
    const reports = (data.directReports || []).length
      ? `<div class="tree-label" style="margin-top:1rem;">Direct reports (${data.directReports.length})</div>` +
        data.directReports.map(r => `<div class="tree-report"><div><div style="font-weight:600;">${esc(r.fullName)}</div><div class="tree-sub">${esc(r.role)}</div></div>${renderStatusBadge(r.status)}</div>`).join("")
      : `<div class="tree-sub" style="margin-top:0.75rem;">No direct reports.</div>`;
    content.innerHTML = mgr +
      `<div class="tree-block selected"><div class="tree-label" style="color:var(--info)">Selected employee</div><div class="tree-name">${esc(e.fullName)}</div><div class="tree-sub">${esc(e.role)} &bull; ${esc(e.departmentName)}</div><div style="margin-top:0.4rem;">${renderStatusBadge(e.status)}</div></div>` +
      reports;
    openModal("hierarchy-modal");
  } catch (err) {
    showToast("Failed to load hierarchy: " + err.message, "error");
  }
}

// ==============================================================================
// 3. LEAVE DESK
// ==============================================================================
const LEAVE_ALLOWANCE = { ANNUAL: 20, SICK: 10, CASUAL: 8, MATERNITY: 90 };
const LEAVE_FIELD_MAP = { employeeId: "leave-emp-select", leaveType: "leave-type-select", startDate: "leave-start-date", endDate: "leave-end-date", reason: "leave-reason" };

async function loadLeaves() {
  const body = $("leave-table-body");
  if (!body) return;
  body.innerHTML = skeletonRows(7);

  try {
    const [all, leaves] = await Promise.all([
      window.EMS_API.getLeaves({}),
      window.EMS_API.getLeaves($("leave-status-filter")?.value ? { status: $("leave-status-filter").value } : {})
    ]);
    // KPI counters always reflect all requests, independent of the active filter
    setText("count-pending", all.filter(l => l.status === "PENDING").length);
    setText("count-approved", all.filter(l => l.status === "APPROVED").length);
    setText("count-rejected", all.filter(l => l.status === "REJECTED").length);

    if (leaves.length === 0) {
      body.innerHTML = emptyRow(7, ICONS.calendar, "No leave requests found");
      return;
    }
    const canDecideLeave = window.EMS_API.hasPermission("LEAVE_DECIDE");
    body.innerHTML = leaves.map(l => `
      <tr>
        <td><div style="font-weight:600;">${esc(l.employeeName)}</div><div class="user-email">${esc(l.employeeEmail || "-")}</div></td>
        <td><span class="badge badge-info">${esc(l.leaveType)}</span></td>
        <td><div>${formatDate(l.startDate)} &rarr; ${formatDate(l.endDate)}</div><div class="font-mono">${l.daysCount} day(s)</div></td>
        <td style="max-width:280px; color:var(--text-muted); font-size:0.85rem;">${esc(l.reason)}</td>
        <td><div class="font-mono">${formatRelativeTime(l.appliedAt)}</div></td>
        <td>${renderStatusBadge(l.status)}</td>
        <td style="text-align:right;">
          ${l.status === "PENDING" ? (canDecideLeave ? `
            <div class="table-row-actions">
              <button class="btn btn-success btn-sm" data-action="approve" data-id="${l.id}" aria-label="Approve leave for ${esc(l.employeeName)}">${ICONS.check} Approve</button>
              <button class="btn btn-danger btn-sm" data-action="reject" data-id="${l.id}" aria-label="Reject leave for ${esc(l.employeeName)}">${ICONS.cross} Reject</button>
            </div>` : `<span style="color:var(--text-dim); font-size:0.75rem;">Awaiting Manager</span>`)
          : `<span style="color:var(--text-dim); font-size:0.75rem;">Completed</span>`}
        </td>
      </tr>`).join("");
  } catch (err) {
    body.innerHTML = errorRow(7, "Error: " + err.message);
    showToast("Failed to load leaves: " + err.message, "error");
  }
}

/** Per-employee balance tracker computed from approved + pending leave in the current year. */
async function loadBalance() {
  const grid = $("balance-grid");
  const empId = $("balance-emp-select")?.value;
  if (!grid) return;
  if (!empId) {
    grid.innerHTML = `<div class="tree-sub">Select an employee to see their leave balance for ${new Date().getFullYear()}.</div>`;
    return;
  }
  try {
    const leaves = await window.EMS_API.getLeaves({ employeeId: empId });
    const year = new Date().getFullYear();
    grid.innerHTML = Object.entries(LEAVE_ALLOWANCE).map(([type, allowance]) => {
      const mine = leaves.filter(l => l.leaveType === type && new Date(l.startDate).getFullYear() === year);
      const used = mine.filter(l => l.status === "APPROVED").reduce((s, l) => s + l.daysCount, 0);
      const pending = mine.filter(l => l.status === "PENDING").reduce((s, l) => s + l.daysCount, 0);
      const pct = Math.min(100, Math.round((used / allowance) * 100));
      return `
        <div class="balance-item">
          <div class="balance-head"><span>${type}</span><span>${Math.max(0, allowance - used)} left</span></div>
          <div class="dist-track" role="progressbar" aria-valuenow="${pct}" aria-valuemin="0" aria-valuemax="100" aria-label="${type} leave used">
            <div class="dist-fill ${used > allowance ? "over" : ""}" style="width:${pct}%"></div>
          </div>
          <div class="balance-sub">${used}/${allowance} used &middot; ${pending} pending</div>
        </div>`;
    }).join("");
  } catch (err) {
    grid.innerHTML = `<div class="field-error">${esc(err.message)}</div>`;
  }
}

async function actOnLeave(id, action) {
  try {
    if (action === "approve") {
      await window.EMS_API.approveLeave(id, "Approved by department lead");
      showToast("Leave request approved", "success");
    } else {
      await window.EMS_API.rejectLeave(id, "Rejected due to project deadlines");
      showToast("Leave request rejected", "warning");
    }
    loadLeaves();
    loadBalance();
  } catch (err) {
    showToast(`${action === "approve" ? "Approval" : "Rejection"} failed: ` + err.message, "error");
  }
}

async function initLeavePage() {
  try {
    const employees = (await window.EMS_API.getAllEmployeesList()).filter(e => e.status !== "TERMINATED");
    const opts = employees.map(e => `<option value="${e.id}">${esc(e.fullName)} (${esc(e.departmentCode)})</option>`).join("");
    if ($("leave-emp-select")) $("leave-emp-select").innerHTML = `<option value="">Select Employee</option>` + opts;
    if ($("balance-emp-select")) $("balance-emp-select").innerHTML = `<option value="">Select Employee</option>` + opts;
  } catch (e) {
    console.error("Error loading employees for leave", e);
  }

  $("leave-status-filter")?.addEventListener("change", loadLeaves);
  $("balance-emp-select")?.addEventListener("change", loadBalance);
  $("leave-start-date")?.addEventListener("change", () => {
    const end = $("leave-end-date");
    end.min = $("leave-start-date").value;
    if (end.value && end.value < end.min) end.value = end.min;
  });

  $("leave-table-body")?.addEventListener("click", (e) => {
    const btn = e.target.closest("button[data-action]");
    if (btn) actOnLeave(Number(btn.dataset.id), btn.dataset.action);
  });

  $("leave-form")?.addEventListener("submit", async (e) => {
    e.preventDefault();
    const form = e.currentTarget;
    clearFormErrors(form);
    const payload = {
      employeeId: parseInt($("leave-emp-select").value, 10),
      leaveType: $("leave-type-select").value,
      startDate: $("leave-start-date").value,
      endDate: $("leave-end-date").value,
      reason: $("leave-reason").value.trim()
    };
    if (payload.endDate < payload.startDate) {
      showFieldError("leave-end-date", "End date cannot be before the start date.");
      return;
    }
    const submit = form.querySelector('button[type="submit"]');
    submit.disabled = true;
    try {
      await window.EMS_API.applyLeave(payload);
      showToast("Leave application submitted", "success");
      closeModal("leave-modal");
      form.reset();
      loadLeaves();
      loadBalance();
    } catch (err) {
      if (!applyServerErrors(form, LEAVE_FIELD_MAP, err)) showToast(err.message, "error");
    } finally {
      submit.disabled = false;
    }
  });

  loadBalance();
  loadLeaves();
}

function openApplyLeaveModal() {
  const form = $("leave-form");
  form.reset();
  clearFormErrors(form);
  const today = new Date().toISOString().split("T")[0];
  $("leave-start-date").value = today;
  $("leave-end-date").value = today;
  $("leave-end-date").min = today;
  openModal("leave-modal");
}

// ==============================================================================
// 4. ASSETS
// ==============================================================================
let activeAssetForCheckout = null;
const ASSET_FIELD_MAP = { assetTag: "asset-form-tag", name: "asset-form-name", category: "asset-form-category", serialNumber: "asset-form-sn", status: "asset-form-status" };

async function loadAssets() {
  const body = $("asset-table-body");
  if (!body) return;
  body.innerHTML = skeletonRows(7);

  const params = {};
  if ($("asset-status-filter")?.value) params.status = $("asset-status-filter").value;
  if ($("asset-category-filter")?.value) params.category = $("asset-category-filter").value;

  try {
    const assets = await window.EMS_API.getAssets(params);
    if (assets.length === 0) {
      body.innerHTML = emptyRow(7, ICONS.laptop, "No hardware assets found");
      return;
    }
    const canWriteAsset = window.EMS_API.hasPermission("ASSET_WRITE");
    body.innerHTML = assets.map(a => `
      <tr>
        <td><div style="font-weight:700; font-family:var(--font-mono);">${esc(a.assetTag)}</div></td>
        <td><div style="font-weight:600;">${esc(a.name)}</div><div class="font-mono">SN: ${esc(a.serialNumber)}</div></td>
        <td><span class="badge badge-info">${esc(String(a.category).replace(/_/g, " "))}</span></td>
        <td>${a.assignedEmployeeName
          ? `<div style="font-weight:600;">${esc(a.assignedEmployeeName)}</div><div class="user-email">${esc(a.assignedEmployeeDepartment || "")}</div>`
          : `<span style="color:var(--text-dim);">Available / Unassigned</span>`}</td>
        <td><div class="font-mono">${a.assignedDate ? formatDate(a.assignedDate) : "-"}</div></td>
        <td>${renderStatusBadge(a.status)}</td>
        <td style="text-align:right;">
          ${canWriteAsset ? `
          <div class="table-row-actions">
            ${a.status === "IN_STOCK" ? `
              <button class="btn btn-primary btn-sm" data-action="checkout" data-id="${a.id}" data-tag="${esc(a.assetTag)}" data-name="${esc(a.name)}">${ICONS.plus} Checkout</button>
              <button class="btn btn-secondary btn-sm" data-action="repair" data-id="${a.id}">${ICONS.wrench} Repair</button>` : ""}
            ${a.status === "ASSIGNED" ? `<button class="btn btn-secondary btn-sm" data-action="return" data-id="${a.id}">${ICONS.refresh} Return</button>` : ""}
            ${a.status === "UNDER_REPAIR" ? `<button class="btn btn-success btn-sm" data-action="return" data-id="${a.id}">${ICONS.check} Repaired</button>` : ""}
          </div>` : `<span style="color:var(--text-dim); font-size:0.75rem;">View only</span>`}
        </td>
      </tr>`).join("");
  } catch (err) {
    body.innerHTML = errorRow(7, "Error: " + err.message);
    showToast("Failed to load assets: " + err.message, "error");
  }
}

async function initAssetsPage() {
  try {
    const employees = (await window.EMS_API.getAllEmployeesList()).filter(e => e.status !== "TERMINATED");
    const opts = employees.map(e => `<option value="${e.id}">${esc(e.fullName)} (${esc(e.departmentCode)})</option>`).join("");
    if ($("checkout-emp-select")) $("checkout-emp-select").innerHTML = `<option value="">Select Employee to Assign</option>` + opts;
    if ($("asset-form-employee")) $("asset-form-employee").innerHTML = `<option value="">Unassigned (In Stock)</option>` + opts;
  } catch (e) {
    console.error("Error loading employees for assets", e);
  }

  $("asset-status-filter")?.addEventListener("change", loadAssets);
  $("asset-category-filter")?.addEventListener("change", loadAssets);

  $("asset-table-body")?.addEventListener("click", async (e) => {
    const btn = e.target.closest("button[data-action]");
    if (!btn) return;
    const id = Number(btn.dataset.id);
    try {
      if (btn.dataset.action === "checkout") {
        openCheckoutModal(id, btn.dataset.tag, btn.dataset.name);
      } else if (btn.dataset.action === "return") {
        await window.EMS_API.returnAsset(id);
        showToast("Asset returned to central inventory", "success");
        loadAssets();
      } else if (btn.dataset.action === "repair") {
        await window.EMS_API.repairAsset(id);
        showToast("Asset moved to Under Repair", "warning");
        loadAssets();
      }
    } catch (err) {
      showToast("Action failed: " + err.message, "error");
    }
  });

  // Assigning an employee implies ASSIGNED; keep the status select consistent
  $("asset-form-employee")?.addEventListener("change", () => {
    if ($("asset-form-employee").value) $("asset-form-status").value = "ASSIGNED";
    else if ($("asset-form-status").value === "ASSIGNED") $("asset-form-status").value = "IN_STOCK";
  });

  $("asset-form")?.addEventListener("submit", async (e) => {
    e.preventDefault();
    const form = e.currentTarget;
    clearFormErrors(form);
    const payload = {
      assetTag: $("asset-form-tag").value.trim().toUpperCase(),
      name: $("asset-form-name").value.trim(),
      category: $("asset-form-category").value,
      serialNumber: $("asset-form-sn").value.trim(),
      status: $("asset-form-status").value,
      assignedEmployeeId: $("asset-form-employee").value ? parseInt($("asset-form-employee").value, 10) : null
    };
    const submit = form.querySelector('button[type="submit"]');
    submit.disabled = true;
    try {
      await window.EMS_API.createAsset(payload);
      showToast(`Asset ${payload.assetTag} added to inventory`, "success");
      closeModal("asset-modal");
      form.reset();
      loadAssets();
    } catch (err) {
      if (!applyServerErrors(form, ASSET_FIELD_MAP, err)) {
        if (err.status === 409) showFieldError("asset-form-tag", err.message);
        else showToast(err.message, "error");
      }
    } finally {
      submit.disabled = false;
    }
  });

  $("checkout-form")?.addEventListener("submit", async (e) => {
    e.preventDefault();
    const empId = parseInt($("checkout-emp-select").value, 10);
    if (!activeAssetForCheckout || !empId) return;
    try {
      await window.EMS_API.checkoutAsset(activeAssetForCheckout, empId);
      showToast("Asset checked out to employee", "success");
      closeModal("checkout-modal");
      loadAssets();
    } catch (err) {
      showToast(err.message, "error");
    }
  });

  loadAssets();
}

function openCreateAssetModal() {
  const form = $("asset-form");
  form.reset();
  clearFormErrors(form);
  openModal("asset-modal");
}

function openCheckoutModal(id, tag, name) {
  activeAssetForCheckout = id;
  setText("checkout-asset-tag", `${tag} - ${name}`);
  $("checkout-form").reset();
  openModal("checkout-modal");
}

// ==============================================================================
// Shared application shell: sidebar, top bar, global search, notifications
// ==============================================================================
const NAV_GROUPS = [
  { label: "Overview", items: [{ href: "index.html", text: "Dashboard", icon: ICONS.dashboard }] },
  { label: "Workforce", items: [
    { href: "employees.html", text: "People", icon: ICONS.users },
    { href: "leave.html", text: "Time Off", icon: ICONS.calendar, countId: "nav-leave-count" }
  ] },
  { label: "Operations", items: [{ href: "assets.html", text: "Assets", icon: ICONS.laptop }] }
];

const DEMO_ROLE_CREDS = {
  ADMIN: { user: "admin", pass: "admin123", label: "Administrator" },
  HR_MANAGER: { user: "hr", pass: "hr123", label: "HR Manager" },
  MANAGER: { user: "manager", pass: "manager123", label: "Department Manager" },
  EMPLOYEE: { user: "employee", pass: "emp123", label: "Employee" }
};

function applyRoleToUI(user) {
  if (!user) return;
  const initials = (user.displayName || user.username || "U")
    .split(" ")
    .map(w => w[0])
    .slice(0, 2)
    .join("")
    .toUpperCase();

  const avatarEl = $("topbar-user-avatar");
  if (avatarEl) {
    avatarEl.textContent = initials;
    avatarEl.style.background = avatarColor(user.displayName || user.username);
  }
  setText("topbar-user-name", user.displayName || user.username);
  const roleBadge = $("topbar-user-role");
  if (roleBadge) {
    roleBadge.className = `role-pill role-pill-${user.role}`;
    roleBadge.textContent = user.roleLabel || user.role;
  }

  setText("dropdown-user-name", user.displayName || user.username);
  setText("dropdown-user-meta", `${user.jobTitle || user.roleLabel} • ${user.departmentName || "Enterprise"}`);

  document.querySelectorAll(".user-dropdown-item[data-switch-role]").forEach(btn => {
    btn.classList.toggle("active-role", btn.dataset.switchRole === user.role);
  });

  // Hide/show page hero write buttons based on RBAC permissions
  const canWriteEmp = window.EMS_API.hasPermission("EMPLOYEE_WRITE");
  const canWriteAsset = window.EMS_API.hasPermission("ASSET_WRITE");
  document.querySelectorAll('[onclick*="openCreateEmployeeModal"]').forEach(btn => {
    btn.style.display = canWriteEmp ? "" : "none";
  });
  document.querySelectorAll('[onclick*="openCreateAssetModal"]').forEach(btn => {
    btn.style.display = canWriteAsset ? "" : "none";
  });

  // Render or update RBAC context bar inside page-container
  const main = document.querySelector("main.page-container");
  if (main) {
    let rbacBar = $("rbac-context-bar");
    if (!rbacBar) {
      rbacBar = document.createElement("div");
      rbacBar.id = "rbac-context-bar";
      rbacBar.className = "rbac-context-bar";
      main.prepend(rbacBar);
    }
    const roleDescriptions = {
      ADMIN: "Full administrative access: workforce CRUD, salary visibility, leave approvals, and asset control.",
      HR_MANAGER: "HR Operations access: employee onboarding/editing, compensation visibility, leave decisions, and asset assignment.",
      MANAGER: "Department Manager access: directory read-only (colleague salaries restricted), team leave approvals.",
      EMPLOYEE: "Employee Self-Service access: directory browsing (salaries restricted), personal leave requests, and asset viewing."
    };
    rbacBar.innerHTML = `
      <div style="display:flex; align-items:center; gap:0.6rem; flex-wrap:wrap;">
        <span class="role-pill role-pill-${esc(user.role)}">${esc(user.roleLabel || user.role)}</span>
        <span>Signed in as <strong>${esc(user.displayName)}</strong> (${esc(user.jobTitle || user.username)}) &mdash; ${esc(roleDescriptions[user.role] || "")}</span>
      </div>
      <div style="display:flex; align-items:center; gap:0.4rem;">
        <button type="button" class="btn btn-secondary btn-sm" id="rbac-bar-logout-btn">${ICONS.logout} Sign out</button>
      </div>`;
    $("rbac-bar-logout-btn")?.addEventListener("click", async () => {
      await window.EMS_API.logout();
      window.location.href = "login.html";
    });
  }
}

function buildShell() {
  const main = document.querySelector("main.page-container");
  if (!main || document.querySelector(".app-layout")) return;

  const page = window.location.pathname.split("/").pop() || "index.html";
  const title = document.body.dataset.title || BRAND.name;
  const cachedUser = window.EMS_API.getStoredUser();

  const nav = NAV_GROUPS.map(g => `
    <div class="nav-section-label">${g.label}</div>
    ${g.items.map(i => `
      <a href="${i.href}" class="nav-link${i.href === page ? " active" : ""}"${i.href === page ? ' aria-current="page"' : ""}>
        ${i.icon.replace("<svg", '<svg class="nav-icon"')}
        <span>${i.text}</span>
        ${i.countId ? `<span class="nav-count" id="${i.countId}"></span>` : ""}
      </a>`).join("")}`).join("");

  const layout = document.createElement("div");
  layout.className = "app-layout";
  layout.innerHTML = `
    <aside class="sidebar">
      <a class="sidebar-brand" href="index.html" aria-label="${BRAND.name} home">
        ${LOGO_SVG}
        <div><div class="brand-name">${BRAND.name}</div><div class="brand-tag">${BRAND.tagline}</div></div>
      </a>
      <nav class="app-nav" aria-label="Primary">${nav}</nav>
      <div class="sidebar-footer">
        <div class="system-status-indicator" role="status">
          <span id="status-dot" class="status-dot"></span>
          <span id="status-text">Connected</span>
        </div>
        <a href="/h2-console" target="_blank" rel="noopener" class="h2-link-btn">${ICONS.database.replace("<svg", '<svg style="width:15px;height:15px;fill:none;stroke:currentColor;stroke-width:1.8"')} Database console</a>
      </div>
    </aside>
    <div class="app-main">
      <div id="offline-banner" class="offline-banner" role="status">
        ${ICONS.alert}<span>Offline mode: showing locally cached sample data. Changes will not be saved.</span>
      </div>
      <header class="topbar">
        <div class="breadcrumb"><span>${BRAND.name}</span><span>/</span><strong>${esc(title)}</strong></div>
        <div class="topbar-spacer"></div>
        <form class="topbar-search" id="global-search" role="search">
          ${ICONS.search}
          <input type="search" id="global-search-input" placeholder="Search people by name, email or role" aria-label="Search people">
        </form>
        <a href="leave.html" class="icon-btn" aria-label="Pending approvals" title="Pending approvals">
          ${ICONS.bell}<span class="badge-dot" id="notif-count"></span>
        </a>
        <div class="user-menu-wrap" id="user-menu-wrap">
          <button type="button" class="user-chip-btn" id="user-chip-btn" aria-haspopup="true" aria-expanded="false" title="Account &amp; Role Switcher">
            <div class="avatar-circle" id="topbar-user-avatar" style="background:${avatarColor(cachedUser?.displayName || "Admin")}" aria-hidden="true">MV</div>
            <div class="user-chip-meta">
              <strong id="topbar-user-name">${esc(cachedUser?.displayName || "Loading...")}</strong>
              <span id="topbar-user-role" class="role-pill role-pill-${esc(cachedUser?.role || "ADMIN")}">${esc(cachedUser?.roleLabel || "Role")}</span>
            </div>
          </button>
          <div class="user-dropdown" id="user-dropdown" role="menu">
            <div class="user-dropdown-header">
              <strong id="dropdown-user-name">${esc(cachedUser?.displayName || "User")}</strong>
              <span id="dropdown-user-meta">${esc(cachedUser?.jobTitle || "")}</span>
            </div>
            <div class="user-dropdown-label">Instant Demo Role Switcher</div>
            <button type="button" class="user-dropdown-item" data-switch-role="ADMIN">
              <span>Marcus Vance</span>
              <span class="role-pill role-pill-ADMIN">Admin</span>
            </button>
            <button type="button" class="user-dropdown-item" data-switch-role="HR_MANAGER">
              <span>Elena Rostova</span>
              <span class="role-pill role-pill-HR_MANAGER">HR Mgr</span>
            </button>
            <button type="button" class="user-dropdown-item" data-switch-role="MANAGER">
              <span>Alexander Wright</span>
              <span class="role-pill role-pill-MANAGER">Manager</span>
            </button>
            <button type="button" class="user-dropdown-item" data-switch-role="EMPLOYEE">
              <span>Liam O'Connor</span>
              <span class="role-pill role-pill-EMPLOYEE">Employee</span>
            </button>
            <button type="button" class="user-dropdown-item danger" id="topbar-logout-btn">
              <span>Sign out of Meridian</span>
              ${ICONS.logout}
            </button>
          </div>
        </div>
      </header>
      <div id="page-slot"></div>
    </div>`;

  document.body.prepend(layout);
  layout.querySelector("#page-slot").replaceWith(main);

  main.id = "main-content";
  const skip = document.createElement("a");
  skip.href = "#main-content";
  skip.className = "skip-link";
  skip.textContent = "Skip to main content";
  document.body.prepend(skip);

  if (cachedUser) {
    applyRoleToUI(cachedUser);
  }

  // Toggle user dropdown
  const chipBtn = $("user-chip-btn");
  const dropdown = $("user-dropdown");
  chipBtn?.addEventListener("click", (e) => {
    e.stopPropagation();
    const isOpen = dropdown.classList.toggle("open");
    chipBtn.setAttribute("aria-expanded", String(isOpen));
  });
  document.addEventListener("click", (e) => {
    if (dropdown && !e.target.closest("#user-menu-wrap")) {
      dropdown.classList.remove("open");
      chipBtn?.setAttribute("aria-expanded", "false");
    }
  });

  // Instant Role Switcher handlers
  document.querySelectorAll(".user-dropdown-item[data-switch-role]").forEach(btn => {
    btn.addEventListener("click", async () => {
      const roleKey = btn.dataset.switchRole;
      const creds = DEMO_ROLE_CREDS[roleKey];
      if (!creds) return;
      try {
        const user = await window.EMS_API.login(creds.user, creds.pass);
        dropdown.classList.remove("open");
        applyRoleToUI(user);
        showToast(`Switched active role to ${user.roleLabel} (${user.displayName})`, "info");
        setTimeout(() => window.location.reload(), 300);
      } catch (err) {
        showToast("Role switch failed: " + err.message, "error");
      }
    });
  });

  // Logout handler
  $("topbar-logout-btn")?.addEventListener("click", async () => {
    await window.EMS_API.logout();
    window.location.href = "login.html";
  });

  $("global-search").addEventListener("submit", (e) => {
    e.preventDefault();
    const q = $("global-search-input").value.trim();
    window.location.href = "employees.html" + (q ? `?q=${encodeURIComponent(q)}` : "");
  });

  // Verify server session on page load
  window.EMS_API.getCurrentUser().then(user => {
    if (!user) {
      window.location.href = "login.html";
      return;
    }
    applyRoleToUI(user);
    return window.EMS_API.getAnalyticsSummary();
  }).then(s => {
    if (!s) return;
    const n = s.pendingLeavesCount ? String(s.pendingLeavesCount) : "";
    setText("nav-leave-count", n);
    setText("notif-count", n);
  }).catch(() => { /* handled by api.js */ });
}

document.addEventListener("DOMContentLoaded", buildShell);

// Expose handlers used by inline onclick attributes in the static HTML shells
Object.assign(window, {
  openModal, closeModal,
  openCreateEmployeeModal, openApplyLeaveModal, openCreateAssetModal,
  initDashboardPage, initEmployeesPage, initLeavePage, initAssetsPage
});
