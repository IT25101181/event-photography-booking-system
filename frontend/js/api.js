// ─── API Configuration ──────────────────────────────────────────────
const API_BASE = 'http://localhost:8080/api';

// ─── Token management ──────────────────────────────────────────────
const Auth = {
    getToken: () => localStorage.getItem('pb_token'),
    getUser:  () => JSON.parse(localStorage.getItem('pb_user') || 'null'),
    setSession: (token, user) => {
        localStorage.setItem('pb_token', token);
        localStorage.setItem('pb_user', JSON.stringify(user));
    },
    clearSession: () => {
        localStorage.removeItem('pb_token');
        localStorage.removeItem('pb_user');
    },
    isLoggedIn: () => !!localStorage.getItem('pb_token'),
    requireAuth: () => {
        if (!localStorage.getItem('pb_token')) {
            window.location.href = 'index.html';
            return false;
        }
        return true;
    }
};

// ─── HTTP Client ────────────────────────────────────────────────────
const http = {
    headers: () => ({
        'Content-Type': 'application/json',
        ...(Auth.getToken() ? { 'Authorization': `Bearer ${Auth.getToken()}` } : {})
    }),

    get: async (path) => {
        const res = await fetch(`${API_BASE}${path}`, { headers: http.headers() });
        return http.handle(res);
    },

    post: async (path, body) => {
        const res = await fetch(`${API_BASE}${path}`, {
            method: 'POST',
            headers: http.headers(),
            body: JSON.stringify(body)
        });
        return http.handle(res);
    },

    put: async (path, body) => {
        const res = await fetch(`${API_BASE}${path}`, {
            method: 'PUT',
            headers: http.headers(),
            body: JSON.stringify(body)
        });
        return http.handle(res);
    },

    patch: async (path, body = null) => {
        const res = await fetch(`${API_BASE}${path}`, {
            method: 'PATCH',
            headers: http.headers(),
            ...(body ? { body: JSON.stringify(body) } : {})
        });
        return http.handle(res);
    },

    delete: async (path) => {
        const res = await fetch(`${API_BASE}${path}`, {
            method: 'DELETE',
            headers: http.headers()
        });
        if (res.status === 204) return { success: true };
        return http.handle(res);
    },

    handle: async (res) => {
        const text = await res.text();
        let data;
        try {
            data = text ? JSON.parse(text) : {};
        } catch (e) {
            data = { message: text };
        }

        if (!res.ok) {
            throw { status: res.status, message: data.message || 'Request failed', data };
        }
        return data;
    }
};

// ─── API Service Layer ──────────────────────────────────────────────
const API = {
    // Auth
    auth: {
        login:    (body) => http.post('/auth/login', body),
        register: (body) => http.post('/auth/register', body)
    },

    // Users
    users: {
        getAll:   () => http.get('/users'),
        getById:  (id) => http.get(`/users/${id}`),
        create:   (body) => http.post('/users/register', body),
        update:   (id, body) => http.put(`/users/${id}`, body),
        delete:   (id) => http.delete(`/users/${id}`),
        toggle:   (id) => http.patch(`/users/${id}/toggle-status`)
    },

    // Media Staff
    staff: {
        getAll:      () => http.get('/media-staff'),
        getById:     (id) => http.get(`/media-staff/${id}`),
        getByType:   (type) => http.get(`/media-staff/type/${type}`),
        getAvailable:() => http.get('/media-staff/available'),
        create:      (body) => http.post('/media-staff', body),
        update:      (id, body) => http.put(`/media-staff/${id}`, body),
        delete:      (id) => http.delete(`/media-staff/${id}`),
        toggle:      (id) => http.patch(`/media-staff/${id}/toggle-availability`)
    },

    // Packages
    packages: {
        getAll:      () => http.get('/packages'),
        getById:     (id) => http.get(`/packages/${id}`),
        getByEvent:  (type) => http.get(`/packages/event/${type}`),
        create:      (body) => http.post('/packages', body),
        update:      (id, body) => http.put(`/packages/${id}`, body),
        delete:      (id) => http.delete(`/packages/${id}`)
    },

    // Bookings
    bookings: {
        getAll:     () => http.get('/bookings'),
        getById:    (id) => http.get(`/bookings/${id}`),
        getByUser:  (uid) => http.get(`/bookings/user/${uid}`),
        getByStatus:(s) => http.get(`/bookings/status/${s}`),
        create:     (body) => http.post('/bookings', body),
        update:     (id, body) => http.put(`/bookings/${id}`, body),
        confirm:    (id) => http.patch(`/bookings/${id}/confirm`),
        cancel:     (id) => http.patch(`/bookings/${id}/cancel`),
        complete:   (id) => http.patch(`/bookings/${id}/complete`),
        delete:     (id) => http.delete(`/bookings/${id}`)
    },

    // Reviews
    reviews: {
        getAll:       () => http.get('/reviews'),
        getById:      (id) => http.get(`/reviews/${id}`),
        getByStaff:   (sid) => http.get(`/reviews/staff/${sid}`),
        getByUser:    (uid) => http.get(`/reviews/user/${uid}`),
        getAvgRating: (sid) => http.get(`/reviews/staff/${sid}/rating`),
        create:       (body) => http.post('/reviews', body),
        update:       (id, body) => http.put(`/reviews/${id}`, body),
        delete:       (id) => http.delete(`/reviews/${id}`)
    },

    // Admins
    admins: {
        getAll:     () => http.get('/admins'),
        getActive:  () => http.get('/admins/active'),
        getById:    (id) => http.get(`/admins/${id}`),
        create:     (body) => http.post('/admins', body),
        update:     (id, body) => http.put(`/admins/${id}`, body),
        toggle:     (id) => http.patch(`/admins/${id}/toggle-status`),
        delete:     (id) => http.delete(`/admins/${id}`)
    }
};

// ─── Toast Notifications ────────────────────────────────────────────
const Toast = {
    container: null,
    init() {
        this.container = document.createElement('div');
        this.container.className = 'toast-container-custom';
        document.body.appendChild(this.container);
    },
    show(message, type = 'info', duration = 3500) {
        if (!this.container) this.init();
        const icons = { success: 'fa-check-circle', error: 'fa-times-circle', warning: 'fa-exclamation-triangle', info: 'fa-info-circle' };
        const colors = { success: '#2ECC71', error: '#E74C3C', warning: '#F39C12', info: '#3498DB' };
        const t = document.createElement('div');
        t.className = `toast-custom ${type}`;
        t.innerHTML = `<i class="fas ${icons[type]}" style="color:${colors[type]};font-size:1.1rem;flex-shrink:0"></i>
                       <span style="font-size:0.875rem;color:#F0F0F0;flex:1">${message}</span>
                       <button onclick="this.parentElement.remove()" style="background:none;border:none;color:#666;cursor:pointer;padding:0 4px;font-size:1rem">&times;</button>`;
        this.container.appendChild(t);
        setTimeout(() => t.remove(), duration);
    },
    success: (msg) => Toast.show(msg, 'success'),
    error:   (msg) => Toast.show(msg, 'error'),
    warning: (msg) => Toast.show(msg, 'warning'),
    info:    (msg) => Toast.show(msg, 'info')
};

// ─── Utility Helpers ────────────────────────────────────────────────
const Utils = {
    statusBadge(status) {
        const map = {
            PENDING:   'badge-pending',
            CONFIRMED: 'badge-confirmed',
            CANCELLED: 'badge-cancelled',
            COMPLETED: 'badge-completed',
        };
        const icons = {
            PENDING: 'fa-clock', CONFIRMED: 'fa-check-circle',
            CANCELLED: 'fa-times-circle', COMPLETED: 'fa-flag-checkered'
        };
        const cls = map[status] || 'badge-gold';
        return `<span class="badge-status ${cls}"><i class="fas ${icons[status] || 'fa-circle'}" style="font-size:0.6rem"></i>${status}</span>`;
    },

    activeBadge(isActive) {
        return isActive
            ? `<span class="badge-status badge-active"><i class="fas fa-circle" style="font-size:0.5rem"></i>Active</span>`
            : `<span class="badge-status badge-inactive"><i class="fas fa-circle" style="font-size:0.5rem"></i>Inactive</span>`;
    },

    stars(rating) {
        let s = '';
        for (let i = 1; i <= 5; i++) {
            s += `<i class="fas fa-star${i <= rating ? '' : ' empty'}" style="color:${i <= rating ? '#D4A017' : '#333'}"></i>`;
        }
        return `<span class="stars">${s}</span>`;
    },

    formatDate(dateStr) {
        if (!dateStr) return '—';
        return new Date(dateStr).toLocaleDateString('en-US', { year: 'numeric', month: 'short', day: 'numeric' });
    },

    formatDateTime(dateStr) {
        if (!dateStr) return '—';
        return new Date(dateStr).toLocaleString('en-US', { month: 'short', day: 'numeric', year: 'numeric', hour: '2-digit', minute: '2-digit' });
    },

    initials(name = '') {
        return name.split(' ').map(w => w[0]).join('').toUpperCase().slice(0, 2);
    },

    avatarHtml(name, size = 36) {
        return `<div style="width:${size}px;height:${size}px;border-radius:50%;background:linear-gradient(135deg,#D4A017,#A07810);display:flex;align-items:center;justify-content:center;font-size:${size * 0.35}px;font-weight:700;color:#111;flex-shrink:0">${Utils.initials(name)}</div>`;
    },

    confirmDialog(msg) {
        return new Promise(resolve => {
            const ok = window.confirm(msg);
            resolve(ok);
        });
    }
};

// Initialize toast on load
document.addEventListener('DOMContentLoaded', () => Toast.init());
