async function fetchMetrics() {
    const response = await fetch('/api/metrics');
    const data = await response.json();

    const table = document.getElementById('metrics-table');
    table.innerHTML = "";

    data.metrics.forEach(m => {
        let row = table.insertRow();
        row.insertCell(0).innerText = m.name;
        row.insertCell(1).innerText = m.value;
        row.insertCell(2).innerText = new Date(m.timestamp).toLocaleString();
    });
}

setInterval(fetchMetrics, 5000);
fetchMetrics();

