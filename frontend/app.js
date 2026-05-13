// ============================================================
// API Base URL — matches Spring Boot server.port=8080
// ============================================================
const API_BASE = 'http://localhost:8080';

const app = {
    init() {
        this.bindEvents();
        this.initTheme();
        this.loadDashboard();
    },

    bindEvents() {
        // Navigation
        document.querySelectorAll('.nav-item').forEach(item => {
            item.addEventListener('click', (e) => {
                e.preventDefault();
                document.querySelectorAll('.nav-item').forEach(nav => nav.classList.remove('active'));
                item.classList.add('active');
                this.switchTab(item.dataset.target);
            });
        });

        // Theme Toggle
        document.getElementById('theme-toggle').addEventListener('click', () => {
            this.toggleTheme();
        });

        // Issue Tabs (Active / All History)
        document.querySelectorAll('#issues .tab-btn').forEach(btn => {
            btn.addEventListener('click', () => {
                document.querySelectorAll('#issues .tab-btn').forEach(b => b.classList.remove('active'));
                btn.classList.add('active');
                this.loadIssues(btn.dataset.filter);
            });
        });

        // Search Books (live filter)
        document.getElementById('book-search').addEventListener('input', (e) => {
            const keyword = e.target.value.trim();
            if (keyword.length > 0) {
                this.searchBooks(keyword);
            } else {
                this.loadBooks();
            }
        });
    },

    // ──────────────────────────────────────────────
    // Theme helpers
    // ──────────────────────────────────────────────
    initTheme() {
        const saved = localStorage.getItem('theme') || 'dark';
        document.documentElement.setAttribute('data-theme', saved);
        this.updateThemeIcon(saved);
    },

    toggleTheme() {
        const current = document.documentElement.getAttribute('data-theme');
        const next = current === 'dark' ? 'light' : 'dark';
        document.documentElement.setAttribute('data-theme', next);
        localStorage.setItem('theme', next);
        this.updateThemeIcon(next);
    },

    updateThemeIcon(theme) {
        const icon = document.querySelector('#theme-toggle i');
        icon.className = theme === 'dark' ? 'fa-solid fa-sun' : 'fa-solid fa-moon';
    },

    // ──────────────────────────────────────────────
    // Tab / section switching
    // ──────────────────────────────────────────────
    switchTab(targetId) {
        document.querySelectorAll('.view-section').forEach(s => s.classList.remove('active'));
        document.getElementById(targetId).classList.add('active');

        const titleMap = {
            dashboard: 'Dashboard Overview',
            books:     'Library Catalog',
            members:   'Member Directory',
            issues:    'Issue & Return Management'
        };
        document.getElementById('page-title').innerText = titleMap[targetId] || '';

        if (targetId === 'dashboard') this.loadDashboard();
        if (targetId === 'books')     this.loadBooks();
        if (targetId === 'members')   this.loadMembers();
        if (targetId === 'issues')    this.loadIssues('active');
    },

    // ──────────────────────────────────────────────
    // Modal helpers
    // ──────────────────────────────────────────────
    openModal(modalId) {
        document.getElementById(modalId).classList.add('active');
        if (modalId === 'issue-modal') this.populateIssueDropdowns();
    },

    closeModal(modalId) {
        document.getElementById(modalId).classList.remove('active');
        const form = document.querySelector(`#${modalId} form`);
        if (form) form.reset();
    },

    // ──────────────────────────────────────────────
    // Toast notification
    // ──────────────────────────────────────────────
    showToast(message, type = 'success') {
        const container = document.getElementById('toast-container');
        const toast     = document.createElement('div');
        toast.className = `toast ${type}`;

        const iconMap = { success: 'fa-check-circle', error: 'fa-circle-exclamation', warning: 'fa-triangle-exclamation' };
        toast.innerHTML = `
            <div class="toast-icon"><i class="fa-solid ${iconMap[type] || iconMap.success}"></i></div>
            <div class="toast-message">${message}</div>
        `;
        container.appendChild(toast);
        toast.offsetHeight; // force reflow
        toast.classList.add('show');

        setTimeout(() => {
            toast.classList.remove('show');
            setTimeout(() => toast.remove(), 300);
        }, 3500);
    },

    // ──────────────────────────────────────────────
    // Core API helper
    // Updated for root backend's ApiResponse wrapper
    // ──────────────────────────────────────────────
    async apiCall(endpoint, method = 'GET', body = null) {
        const options = {
            method,
            headers: { 'Content-Type': 'application/json' }
        };
        if (body !== null) options.body = JSON.stringify(body);

        const response = await fetch(`${API_BASE}${endpoint}`, options);

        if (response.status === 204) return null;

        const result = await response.json();

        // Handle root project's ApiResponse wrapper: { success, message, data, ... }
        if (result && typeof result === 'object' && 'success' in result) {
            if (!result.success) {
                const msg = result.message || `Request failed (${response.status})`;
                throw new Error(msg);
            }
            return result.data; // Extract the actual payload
        }

        // Fallback for unwrapped responses
        if (!response.ok) {
            const msg = result.message || `Request failed (${response.status})`;
            throw new Error(msg);
        }
        return result;
    },

    // ──────────────────────────────────────────────
    // DASHBOARD
    // ──────────────────────────────────────────────
    async loadDashboard() {
        try {
            const [books, members, activeIssues] = await Promise.all([
                this.apiCall('/books'),
                this.apiCall('/members'),
                this.apiCall('/api/issue-records/active')
            ]);

            // books / members / activeIssues are now correctly extracted from the wrapper
            const availableCount = books.filter(b => b.availability === true).length;

            document.getElementById('stat-total-books').innerText    = books.length;
            document.getElementById('stat-available-books').innerText = availableCount;
            document.getElementById('stat-total-members').innerText  = members.length;
            document.getElementById('stat-active-issues').innerText  = activeIssues.length;

            // Recent active issues table
            const tbody = document.querySelector('#recent-issues-table tbody');
            tbody.innerHTML = '';

            if (activeIssues.length === 0) {
                tbody.innerHTML = `<tr><td colspan="4" style="text-align:center;color:var(--text-muted)">No active issues.</td></tr>`;
                return;
            }

            activeIssues.slice(0, 5).forEach(issue => {
                const tr = document.createElement('tr');
                tr.innerHTML = `
                    <td><strong>${issue.bookTitle}</strong></td>
                    <td>${issue.memberName}</td>
                    <td>${issue.issueDate || '-'}</td>
                    <td><span class="badge warning">Active</span></td>
                `;
                tbody.appendChild(tr);
            });
        } catch (err) {
            this.showToast('Failed to load dashboard: ' + err.message, 'error');
        }
    },

    // ──────────────────────────────────────────────
    // BOOKS
    // GET /books            → BookResponseDTO[]
    // GET /books/search?keyword=  → BookResponseDTO[]
    // POST /books           body: { title, author }
    // ──────────────────────────────────────────────
    async loadBooks() {
        try {
            const books = await this.apiCall('/books');
            this.renderBooksTable(books);
        } catch (err) {
            this.showToast('Failed to load books: ' + err.message, 'error');
        }
    },

    async searchBooks(keyword) {
        try {
            const books = await this.apiCall(`/books/search?keyword=${encodeURIComponent(keyword)}`);
            this.renderBooksTable(books);
        } catch (err) {
            this.showToast('Search failed: ' + err.message, 'error');
        }
    },

    renderBooksTable(books) {
        const tbody = document.querySelector('#books-table tbody');
        tbody.innerHTML = '';

        if (!books || books.length === 0) {
            tbody.innerHTML = `<tr><td colspan="4" style="text-align:center;color:var(--text-muted)">No books found.</td></tr>`;
            return;
        }

        books.forEach(book => {
            const badge = book.availability
                ? '<span class="badge success">Available</span>'
                : '<span class="badge danger">Issued</span>';
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>#${book.bookId}</td>
                <td><strong>${book.title}</strong></td>
                <td>${book.author}</td>
                <td>${badge}</td>
            `;
            tbody.appendChild(tr);
        });
    },

    async submitBook(e) {
        e.preventDefault();
        const title  = document.getElementById('book-title').value.trim();
        const author = document.getElementById('book-author').value.trim();

        try {
            // POST /books  →  BookResponseDTO (the created book)
            await this.apiCall('/books', 'POST', { title, author });
            this.showToast('Book added successfully!', 'success');
            this.closeModal('book-modal');
            this.loadBooks();
            this.loadDashboard();
        } catch (err) {
            this.showToast(err.message, 'error');
        }
    },

    // ──────────────────────────────────────────────
    // MEMBERS
    // GET /members           → MemberResponseDTO[]
    // GET /members/{id}      → MemberResponseDTO
    // POST /members          body: { name, email }
    // GET /members/{id}/books → IssueRecordResponseDTO[]  (via MemberController)
    // ──────────────────────────────────────────────
    async loadMembers() {
        try {
            const members = await this.apiCall('/members');
            const grid = document.getElementById('members-grid');
            grid.innerHTML = '';

            if (!members || members.length === 0) {
                grid.innerHTML = `<div style="color:var(--text-muted);grid-column:1/-1;text-align:center;">No members registered yet.</div>`;
                return;
            }

            members.forEach(member => {
                const card = document.createElement('div');
                card.className = 'member-card glass';
                card.innerHTML = `
                    <div class="member-avatar">${member.name.charAt(0).toUpperCase()}</div>
                    <div class="member-info">
                        <h3>${member.name}</h3>
                        <p><i class="fa-regular fa-envelope"></i> ${member.email}</p>
                        <button class="btn btn-secondary btn-sm"
                            onclick="app.viewMemberIssues(${member.memberId}, '${member.name.replace(/'/g, "\\'")}')">
                            View History
                        </button>
                    </div>
                `;
                grid.appendChild(card);
            });
        } catch (err) {
            this.showToast('Failed to load members: ' + err.message, 'error');
        }
    },

    async submitMember(e) {
        e.preventDefault();
        const name  = document.getElementById('member-name').value.trim();
        const email = document.getElementById('member-email').value.trim();

        try {
            // POST /members  →  MemberResponseDTO
            await this.apiCall('/members', 'POST', { name, email });
            this.showToast('Member registered successfully!', 'success');
            this.closeModal('member-modal');
            this.loadMembers();
            this.loadDashboard();
        } catch (err) {
            this.showToast(err.message, 'error');
        }
    },

    // ──────────────────────────────────────────────
    // ISSUES
    // ──────────────────────────────────────────────
    async loadIssues(type = 'active') {
        try {
            const endpoint = type === 'active'
                ? '/api/issue-records/active'
                : '/api/issue-records';
            const issues = await this.apiCall(endpoint);
            this.renderIssuesTable(issues);
        } catch (err) {
            this.showToast('Failed to load issues: ' + err.message, 'error');
        }
    },

    async viewMemberIssues(memberId, memberName) {
        try {
            const issues = await this.apiCall(`/members/${memberId}/issues`);

            // Switch UI to Issues tab
            document.querySelectorAll('.nav-item').forEach(n => n.classList.remove('active'));
            document.querySelector('.nav-item[data-target="issues"]').classList.add('active');
            document.querySelectorAll('.view-section').forEach(s => s.classList.remove('active'));
            document.getElementById('issues').classList.add('active');
            document.getElementById('page-title').innerText = `Issue History: ${memberName}`;

            // Clear tab highlight
            document.querySelectorAll('#issues .tab-btn').forEach(b => b.classList.remove('active'));

            this.renderIssuesTable(issues);
        } catch (err) {
            this.showToast('Failed to load member history: ' + err.message, 'error');
        }
    },

    renderIssuesTable(issues) {
        const tbody = document.querySelector('#issues-table tbody');
        tbody.innerHTML = '';

        if (!issues || issues.length === 0) {
            tbody.innerHTML = `<tr><td colspan="6" style="text-align:center;color:var(--text-muted)">No issue records found.</td></tr>`;
            return;
        }

        issues.forEach(issue => {
            // returnDate is null → book is still active
            const isActive = issue.returnDate === null || issue.returnDate === undefined;
            const retDate  = isActive ? '-' : issue.returnDate;
            const actionBtn = isActive
                ? `<button class="btn btn-primary" onclick="app.returnBook(${issue.issueId})">Return Book</button>`
                : `<span class="badge success">Returned</span>`;

            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>#${issue.issueId}</td>
                <td><strong>${issue.bookTitle}</strong></td>
                <td>${issue.memberName}</td>
                <td>${issue.issueDate || '-'}</td>
                <td>${retDate}</td>
                <td>${actionBtn}</td>
            `;
            tbody.appendChild(tr);
        });
    },

    async populateIssueDropdowns() {
        const bookSelect   = document.getElementById('issue-book-id');
        const memberSelect = document.getElementById('issue-member-id');

        bookSelect.innerHTML   = '<option value="">Loading...</option>';
        memberSelect.innerHTML = '<option value="">Loading...</option>';

        try {
            // Only show available books in the dropdown
            const [books, members] = await Promise.all([
                this.apiCall('/books/available'),
                this.apiCall('/members')
            ]);

            bookSelect.innerHTML = '<option value="" disabled selected>Select an available book</option>';
            if (books.length === 0) {
                bookSelect.innerHTML += '<option disabled>No available books</option>';
            } else {
                books.forEach(b => {
                    bookSelect.innerHTML += `<option value="${b.bookId}">${b.title} — ${b.author}</option>`;
                });
            }

            memberSelect.innerHTML = '<option value="" disabled selected>Select a member</option>';
            if (members.length === 0) {
                memberSelect.innerHTML += '<option disabled>No members registered</option>';
            } else {
                members.forEach(m => {
                    memberSelect.innerHTML += `<option value="${m.memberId}">${m.name} (${m.email})</option>`;
                });
            }
        } catch (err) {
            this.showToast('Failed to load dropdown data: ' + err.message, 'error');
        }
    },

    async submitIssue(e) {
        e.preventDefault();
        const bookId   = document.getElementById('issue-book-id').value;
        const memberId = document.getElementById('issue-member-id').value;

        if (!bookId || !memberId) {
            this.showToast('Please select both a book and a member.', 'error');
            return;
        }

        try {
            await this.apiCall('/api/issue-records/issue', 'POST', {
                bookId:   parseInt(bookId,   10),
                memberId: parseInt(memberId, 10)
            });
            this.showToast('Book issued successfully!', 'success');
            this.closeModal('issue-modal');

            // Reset to Active tab
            document.querySelectorAll('#issues .tab-btn').forEach(b => b.classList.remove('active'));
            document.querySelector('#issues .tab-btn[data-filter="active"]').classList.add('active');
            this.loadIssues('active');
            this.loadDashboard();
        } catch (err) {
            this.showToast(err.message, 'error');
        }
    },

    async returnBook(issueId) {
        if (!confirm('Mark this book as returned?')) return;
        try {
            // Libary-backend uses: PUT /api/issue-records/return/{issueId}
            await this.apiCall(`/api/issue-records/return/${issueId}`, 'PUT');
            this.showToast('Book returned successfully!', 'success');

            // Refresh current tab
            const activeTabBtn = document.querySelector('#issues .tab-btn.active');
            this.loadIssues(activeTabBtn ? activeTabBtn.dataset.filter : 'active');
            this.loadDashboard();
        } catch (err) {
            this.showToast(err.message, 'error');
        }
    }
};

// Bootstrap
document.addEventListener('DOMContentLoaded', () => app.init());
