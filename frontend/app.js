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

        // Issue Tabs
        document.querySelectorAll('#issues .tab-btn').forEach(btn => {
            btn.addEventListener('click', (e) => {
                document.querySelectorAll('#issues .tab-btn').forEach(b => b.classList.remove('active'));
                btn.classList.add('active');
                if(btn.dataset.filter === 'active') {
                    this.loadIssues('active');
                } else {
                    this.loadIssues('all');
                }
            });
        });

        // Search Books
        document.getElementById('book-search').addEventListener('input', (e) => {
            const keyword = e.target.value.trim();
            if (keyword.length > 0) {
                this.searchBooks(keyword);
            } else {
                this.loadBooks();
            }
        });
    },

    initTheme() {
        const savedTheme = localStorage.getItem('theme') || 'dark';
        document.documentElement.setAttribute('data-theme', savedTheme);
        this.updateThemeIcon(savedTheme);
    },

    toggleTheme() {
        const currentTheme = document.documentElement.getAttribute('data-theme');
        const newTheme = currentTheme === 'dark' ? 'light' : 'dark';
        document.documentElement.setAttribute('data-theme', newTheme);
        localStorage.setItem('theme', newTheme);
        this.updateThemeIcon(newTheme);
    },

    updateThemeIcon(theme) {
        const icon = document.querySelector('#theme-toggle i');
        if (theme === 'dark') {
            icon.className = 'fa-solid fa-sun';
        } else {
            icon.className = 'fa-solid fa-moon';
        }
    },

    switchTab(targetId) {
        document.querySelectorAll('.view-section').forEach(section => {
            section.classList.remove('active');
        });
        document.getElementById(targetId).classList.add('active');
        
        const titleMap = {
            'dashboard': 'Dashboard Overview',
            'books': 'Library Catalog',
            'members': 'Member Directory',
            'issues': 'Issue & Return Management'
        };
        document.getElementById('page-title').innerText = titleMap[targetId];

        // Load data for the tab
        if (targetId === 'dashboard') this.loadDashboard();
        if (targetId === 'books') this.loadBooks();
        if (targetId === 'members') this.loadMembers();
        if (targetId === 'issues') this.loadIssues('active');
    },

    openModal(modalId) {
        document.getElementById(modalId).classList.add('active');
        if (modalId === 'issue-modal') {
            this.populateIssueDropdowns();
        }
    },

    closeModal(modalId) {
        document.getElementById(modalId).classList.remove('active');
        const form = document.querySelector(`#${modalId} form`);
        if (form) form.reset();
    },

    showToast(message, type = 'success') {
        const container = document.getElementById('toast-container');
        const toast = document.createElement('div');
        toast.className = `toast ${type}`;
        
        let iconClass = 'fa-check-circle';
        if (type === 'error') iconClass = 'fa-circle-exclamation';
        if (type === 'warning') iconClass = 'fa-triangle-exclamation';

        toast.innerHTML = `
            <div class="toast-icon"><i class="fa-solid ${iconClass}"></i></div>
            <div class="toast-message">${message}</div>
        `;
        
        container.appendChild(toast);
        
        // Trigger reflow
        toast.offsetHeight; 
        toast.classList.add('show');
        
        setTimeout(() => {
            toast.classList.remove('show');
            setTimeout(() => toast.remove(), 300);
        }, 3000);
    },

    // --- API Calls ---

    async apiCall(endpoint, method = 'GET', body = null) {
        try {
            const options = {
                method,
                headers: { 'Content-Type': 'application/json' }
            };
            if (body) options.body = JSON.stringify(body);
            
            const response = await fetch(`${API_BASE}${endpoint}`, options);
            const data = await response.json();
            
            if (!response.ok) {
                throw new Error(data.message || 'API request failed');
            }
            return data;
        } catch (error) {
            this.showToast(error.message, 'error');
            console.error('API Error:', error);
            throw error;
        }
    },

    async loadDashboard() {
        try {
            const [books, members, activeIssues] = await Promise.all([
                this.apiCall('/books'),
                this.apiCall('/members'),
                this.apiCall('/issues/active')
            ]);

            const availableBooks = books.data.filter(b => b.availability).length;

            document.getElementById('stat-total-books').innerText = books.data.length;
            document.getElementById('stat-available-books').innerText = availableBooks;
            document.getElementById('stat-total-members').innerText = members.data.length;
            document.getElementById('stat-active-issues').innerText = activeIssues.data.length;

            // Populate recent issues
            const tbody = document.querySelector('#recent-issues-table tbody');
            tbody.innerHTML = '';
            
            // Take up to 5 recent issues
            const recent = activeIssues.data.slice(0, 5);
            
            if(recent.length === 0) {
                tbody.innerHTML = `<tr><td colspan="4" style="text-align:center; color:var(--text-muted)">No active issues.</td></tr>`;
                return;
            }

            recent.forEach(issue => {
                const tr = document.createElement('tr');
                tr.innerHTML = `
                    <td><strong>${issue.bookTitle}</strong></td>
                    <td>${issue.memberName}</td>
                    <td>${issue.issueDate}</td>
                    <td><span class="badge warning">Active</span></td>
                `;
                tbody.appendChild(tr);
            });

        } catch(e) {
            console.log("Failed to load dashboard stats", e);
        }
    },

    async loadBooks() {
        try {
            const res = await this.apiCall('/books');
            this.renderBooksTable(res.data);
        } catch(e) {}
    },

    async searchBooks(keyword) {
        try {
            const res = await this.apiCall(`/books/search?keyword=${encodeURIComponent(keyword)}`);
            this.renderBooksTable(res.data);
        } catch(e) {}
    },

    renderBooksTable(books) {
        const tbody = document.querySelector('#books-table tbody');
        tbody.innerHTML = '';
        
        if(books.length === 0) {
            tbody.innerHTML = `<tr><td colspan="4" style="text-align:center; color:var(--text-muted)">No books found.</td></tr>`;
            return;
        }

        books.forEach(book => {
            const statusBadge = book.availability 
                ? '<span class="badge success">Available</span>' 
                : '<span class="badge danger">Issued</span>';
            
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>#${book.bookId}</td>
                <td><strong>${book.title}</strong></td>
                <td>${book.author}</td>
                <td>${statusBadge}</td>
            `;
            tbody.appendChild(tr);
        });
    },

    async submitBook(e) {
        e.preventDefault();
        const title = document.getElementById('book-title').value;
        const author = document.getElementById('book-author').value;

        try {
            const res = await this.apiCall('/books', 'POST', { title, author });
            this.showToast(res.message, 'success');
            this.closeModal('book-modal');
            this.loadBooks();
            this.loadDashboard(); // update stats
        } catch(e) {}
    },

    async loadMembers() {
        try {
            const res = await this.apiCall('/members');
            const grid = document.getElementById('members-grid');
            grid.innerHTML = '';
            
            if(res.data.length === 0) {
                grid.innerHTML = `<div style="color:var(--text-muted); grid-column:1/-1; text-align:center;">No members found.</div>`;
                return;
            }

            res.data.forEach(member => {
                const card = document.createElement('div');
                card.className = 'member-card glass';
                card.innerHTML = `
                    <div class="member-avatar">${member.name.charAt(0).toUpperCase()}</div>
                    <div class="member-info">
                        <h3>${member.name}</h3>
                        <p><i class="fa-regular fa-envelope"></i> ${member.email}</p>
                        <button class="btn btn-secondary btn-sm" onclick="app.viewMemberIssues(${member.memberId}, '${member.name}')">
                            View History
                        </button>
                    </div>
                `;
                grid.appendChild(card);
            });
        } catch(e) {}
    },

    async submitMember(e) {
        e.preventDefault();
        const name = document.getElementById('member-name').value;
        const email = document.getElementById('member-email').value;

        try {
            const res = await this.apiCall('/members', 'POST', { name, email });
            this.showToast(res.message, 'success');
            this.closeModal('member-modal');
            this.loadMembers();
            this.loadDashboard();
        } catch(e) {}
    },

    async loadIssues(type = 'active') {
        try {
            const endpoint = type === 'active' ? '/issues/active' : '/issues';
            const res = await this.apiCall(endpoint);
            this.renderIssuesTable(res.data);
        } catch(e) {}
    },

    async viewMemberIssues(memberId, memberName) {
        try {
            const res = await this.apiCall(`/members/${memberId}/issues`);
            // Switch to issues tab
            document.querySelectorAll('.nav-item').forEach(nav => nav.classList.remove('active'));
            document.querySelector('.nav-item[data-target="issues"]').classList.add('active');
            
            document.querySelectorAll('.view-section').forEach(section => section.classList.remove('active'));
            document.getElementById('issues').classList.add('active');
            
            document.getElementById('page-title').innerText = `Issue History: ${memberName}`;
            
            // Uncheck active tabs
            document.querySelectorAll('#issues .tab-btn').forEach(b => b.classList.remove('active'));
            
            this.renderIssuesTable(res.data);
        } catch(e) {}
    },

    renderIssuesTable(issues) {
        const tbody = document.querySelector('#issues-table tbody');
        tbody.innerHTML = '';
        
        if(issues.length === 0) {
            tbody.innerHTML = `<tr><td colspan="6" style="text-align:center; color:var(--text-muted)">No issues found.</td></tr>`;
            return;
        }

        issues.forEach(issue => {
            let actionBtn = '-';
            let retDate = '-';
            
            if (issue.active) {
                actionBtn = `<button class="btn btn-primary" onclick="app.returnBook(${issue.issueId})">Return Book</button>`;
            } else {
                retDate = issue.returnDate;
            }

            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>#${issue.issueId}</td>
                <td><strong>${issue.bookTitle}</strong></td>
                <td>${issue.memberName}</td>
                <td>${issue.issueDate}</td>
                <td>${retDate}</td>
                <td>${actionBtn}</td>
            `;
            tbody.appendChild(tr);
        });
    },

    async populateIssueDropdowns() {
        const bookSelect = document.getElementById('issue-book-id');
        const memberSelect = document.getElementById('issue-member-id');
        
        bookSelect.innerHTML = '<option value="">Loading...</option>';
        memberSelect.innerHTML = '<option value="">Loading...</option>';

        try {
            const [books, members] = await Promise.all([
                this.apiCall('/books/available'),
                this.apiCall('/members')
            ]);

            bookSelect.innerHTML = '<option value="" disabled selected>Select an available book</option>';
            books.data.forEach(b => {
                bookSelect.innerHTML += `<option value="${b.bookId}">${b.title} by ${b.author}</option>`;
            });

            memberSelect.innerHTML = '<option value="" disabled selected>Select a member</option>';
            members.data.forEach(m => {
                memberSelect.innerHTML += `<option value="${m.memberId}">${m.name} (${m.email})</option>`;
            });
        } catch(e) {}
    },

    async submitIssue(e) {
        e.preventDefault();
        const bookId = document.getElementById('issue-book-id').value;
        const memberId = document.getElementById('issue-member-id').value;

        if(!bookId || !memberId) {
            this.showToast('Please select both a book and a member', 'error');
            return;
        }

        try {
            const res = await this.apiCall('/issues/issue', 'POST', { bookId: parseInt(bookId), memberId: parseInt(memberId) });
            this.showToast(res.message, 'success');
            this.closeModal('issue-modal');
            this.loadIssues('active');
            
            // Reset tab to Active
            document.querySelectorAll('#issues .tab-btn').forEach(b => b.classList.remove('active'));
            document.querySelector('#issues .tab-btn[data-filter="active"]').classList.add('active');
            
        } catch(e) {}
    },

    async returnBook(issueId) {
        try {
            const res = await this.apiCall(`/issues/return/${issueId}`, 'PUT');
            this.showToast(res.message, 'success');
            
            // Refresh current view
            const activeTab = document.querySelector('#issues .tab-btn.active');
            if (activeTab) {
                this.loadIssues(activeTab.dataset.filter);
            } else {
                this.loadIssues('active');
            }
        } catch(e) {}
    }
};

// Initialize app when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    app.init();
});
