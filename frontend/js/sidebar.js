// Shared sidebar renderer
function renderSidebar(activePage) {
    const user = Auth.getUser();
    const isAdmin = user?.role === 'ADMIN' || user?.role === 'SUPER_ADMIN';

    const adminLinks = isAdmin ? `
        <div class="nav-section-label">Admin</div>
        <a class="nav-item ${activePage==='admin'?'active':''}" href="admin-dashboard.html">
            <i class="fas fa-tachometer-alt"></i> Dashboard
        </a>
        <a class="nav-item ${activePage==='users'?'active':''}" href="users.html">
            <i class="fas fa-users"></i> Users
        </a>
        <a class="nav-item ${activePage==='admins'?'active':''}" href="admins.html">
            <i class="fas fa-user-shield"></i> Admins
        </a>
    ` : '';

    return `
    <div class="sidebar" id="sidebar">
        <div class="sidebar-logo">
            <div class="brand">Snapix</div>
            <div class="brand-sub">Management Suite</div>
        </div>

        <div class="sidebar-user">
            <div class="user-avatar">${Utils.initials(user?.fullName || 'U')}</div>
            <div>
                <div class="user-name">${user?.fullName || 'User'}</div>
                <div class="user-role">${user?.role || 'USER'}</div>
            </div>
        </div>

        <div style="padding:16px 10px">
            <button class="btn-new-event" onclick="window.location.href='booking-form.html'">
                <i class="fas fa-plus"></i> New Booking
            </button>
        </div>

        <nav class="sidebar-nav">
            <div class="nav-section-label">Menu</div>
            <a class="nav-item ${activePage==='dashboard'?'active':''}" href="${isAdmin?'admin-dashboard.html':'dashboard.html'}">
                <i class="fas fa-th-large"></i> Dashboard
            </a>
            <a class="nav-item ${activePage==='bookings'?'active':''}" href="bookings.html">
                <i class="fas fa-calendar-alt"></i> Bookings
            </a>
            <a class="nav-item ${activePage==='staff'?'active':''}" href="media-staff.html">
                <i class="fas fa-camera"></i> Media Staff
            </a>
            <a class="nav-item ${activePage==='packages'?'active':''}" href="packages.html">
                <i class="fas fa-box-open"></i> Packages
            </a>
            <a class="nav-item ${activePage==='reviews'?'active':''}" href="reviews.html">
                <i class="fas fa-star"></i> Reviews
            </a>
            ${adminLinks}
        </nav>

        <div class="sidebar-footer">
            <a class="nav-item" href="#" onclick="doLogout()" style="color:#E74C3C">
                <i class="fas fa-sign-out-alt"></i> Logout
            </a>
        </div>
    </div>`;
}

function doLogout() {
    Auth.clearSession();
    Toast.info('Logged out successfully');
    setTimeout(() => window.location.href = 'index.html', 500);
}

function initPage(activePage) {
    if (!Auth.requireAuth()) return;
    document.getElementById('sidebar-root').innerHTML = renderSidebar(activePage);
}
