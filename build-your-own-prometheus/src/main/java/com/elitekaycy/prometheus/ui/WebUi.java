package main.java.com.elitekaycy.prometheus.ui;

public class WebUi {

    public static String serveUi() {
        return """
                        <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <title>Metrics Dashboard</title>
                    <script src="https://cdn.tailwindcss.com"></script>
                    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
                    <style>
                        :root {
                            --bg-primary: #0f172a;
                            --bg-secondary: #1e293b;
                            --text-primary: #e2e8f0;
                            --text-secondary: #94a3b8;
                            --accent-color: #3b82f6;
                        }
                        body {
                            background-color: var(--bg-primary);
                            color: var(--text-primary);
                            font-family: 'Inter', sans-serif;
                        }
                        .card {
                            background-color: var(--bg-secondary);
                            border-radius: 12px;
                            box-shadow: 0 10px 15px -3px rgba(0,0,0,0.1), 0 4px 6px -2px rgba(0,0,0,0.05);
                        }
                        .search-input {
                            background-color: #1e293b;
                            border-color: #334155;
                        }
                        .metric-row:hover {
                            background-color: #2c3444;
                            transition: all 0.3s ease;
                        }
                    </style>
                </head>
                <body class="p-8">
                    <div class="container mx-auto max-w-4xl">
                        <div class="flex justify-between items-center mb-6">
                            <h1 class="text-4xl font-bold text-white">Metrics Dashboard</h1>
                            <div class="flex items-center space-x-4">
                                <div class="relative">
                                    <i class="fas fa-search absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400"></i>
                                    <input
                                        type="text"
                                        id="search-input"
                                        placeholder="Search metrics..."
                                        class="search-input pl-10 pr-4 py-2 w-64 rounded-lg text-white border border-gray-700 focus:outline-none focus:ring-2 focus:ring-blue-500"
                                    >
                                </div>
                                <button id="refresh-btn" class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg transition-colors">
                                    <i class="fas fa-sync"></i>
                                </button>
                            </div>
                        </div>

                        <div class="card p-6">
                            <div class="overflow-x-auto">
                                <table class="w-full">
                                    <thead>
                                        <tr class="border-b border-gray-700">
                                            <th class="p-3 text-left text-gray-400 font-medium">Name</th>
                                            <th class="p-3 text-left text-gray-400 font-medium">Value</th>
                                            <th class="p-3 text-left text-gray-400 font-medium">Timestamp</th>
                                        </tr>
                                    </thead>
                                    <tbody id="metrics-table"></tbody>
                                </table>
                            </div>

                            <div id="pagination" class="flex justify-between items-center mt-4">
                                <button id="prev-btn" class="bg-gray-700 hover:bg-gray-600 text-white px-4 py-2 rounded-lg disabled:opacity-50">
                                    Previous
                                </button>
                                <span id="page-info" class="text-gray-400"></span>
                                <button id="next-btn" class="bg-gray-700 hover:bg-gray-600 text-white px-4 py-2 rounded-lg disabled:opacity-50">
                                    Next
                                </button>
                            </div>
                        </div>

                        <div id="error-toast" class="fixed bottom-4 right-4 bg-red-600 text-white p-4 rounded-lg hidden">
                            <i class="fas fa-exclamation-triangle mr-2"></i>
                            <span id="error-message"></span>
                        </div>
                    </div>

                    <script>
                    class MetricsDashboard {
                        constructor() {
                            this.searchInput = document.getElementById('search-input');
                            this.metricsTable = document.getElementById('metrics-table');
                            this.refreshBtn = document.getElementById('refresh-btn');
                            this.errorToast = document.getElementById('error-toast');
                            this.errorMessage = document.getElementById('error-message');
                            this.prevBtn = document.getElementById('prev-btn');
                            this.nextBtn = document.getElementById('next-btn');
                            this.pageInfo = document.getElementById('page-info');

                            this.currentPage = 1;
                            this.itemsPerPage = 20;
                            this.filteredMetrics = [];

                            this.setupEventListeners();
                            this.fetchMetrics();
                            this.startAutoRefresh();
                        }

                        setupEventListeners() {
                            this.searchInput.addEventListener('input', () => this.filterMetrics());
                            this.refreshBtn.addEventListener('click', () => this.fetchMetrics());
                            this.prevBtn.addEventListener('click', () => this.changePage(-1));
                            this.nextBtn.addEventListener('click', () => this.changePage(1));
                        }

                        async fetchMetrics() {
                            try {
                                const response = await fetch('/api/metrics');
                                const data = await response.json();
                                this.updateMetricsTable(data.metrics);
                            } catch (error) {
                                this.showErrorToast('Failed to fetch metrics');
                            }
                        }

                        updateMetricsTable(metrics) {
                            const searchTerm = this.searchInput.value.toLowerCase();

                            this.filteredMetrics = metrics.filter(m =>
                                m.name.toLowerCase().includes(searchTerm) ||
                                m.value.toString().toLowerCase().includes(searchTerm) ||
                                new Date(m.timestamp).toLocaleString().toLowerCase().includes(searchTerm)
                            );

                            this.currentPage = 1;
                            this.renderTable();
                        }

                        renderTable() {
                            this.metricsTable.innerHTML = "";

                            const startIndex = (this.currentPage - 1) * this.itemsPerPage;
                            const endIndex = startIndex + this.itemsPerPage;
                            const pageMetrics = this.filteredMetrics.slice(startIndex, endIndex);

                            pageMetrics.forEach(m => {
                                let row = this.metricsTable.insertRow();
                                row.classList.add('metric-row', 'hover:bg-gray-800', 'transition-colors');

                                const nameCell = row.insertCell(0);
                                nameCell.innerHTML = `<span class="text-blue-400 font-semibold">${m.name}</span>`;

                                const valueCell = row.insertCell(1);
                                valueCell.innerHTML = `<span class="text-green-400">${m.value}</span>`;

                                const timestampCell = row.insertCell(2);
                                timestampCell.innerHTML = `<span class="text-gray-400">${new Date(m.timestamp).toLocaleString()}</span>`;
                            });

                            this.updatePagination();
                        }

                        updatePagination() {
                            const totalPages = Math.ceil(this.filteredMetrics.length / this.itemsPerPage);

                            this.prevBtn.disabled = this.currentPage === 1;
                            this.nextBtn.disabled = this.currentPage === totalPages;

                            this.pageInfo.textContent = `Page ${this.currentPage} of ${totalPages}`;
                        }

                        changePage(direction) {
                            const totalPages = Math.ceil(this.filteredMetrics.length / this.itemsPerPage);

                            this.currentPage += direction;

                            if (this.currentPage < 1) this.currentPage = 1;
                            if (this.currentPage > totalPages) this.currentPage = totalPages;

                            this.renderTable();
                        }

                        filterMetrics() {
                            this.fetchMetrics();
                        }

                        startAutoRefresh() {
                            setInterval(() => this.fetchMetrics(), 5000);
                        }

                        showErrorToast(message) {
                            this.errorMessage.textContent = message;
                            this.errorToast.classList.remove('hidden');
                            setTimeout(() => {
                                this.errorToast.classList.add('hidden');
                            }, 3000);
                        }
                    }

                    document.addEventListener('DOMContentLoaded', () => new MetricsDashboard());
                    </script>
                </body>
                </html>
                 """;

    }
}
