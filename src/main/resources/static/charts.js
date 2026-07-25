let categoryChart;

function renderChart(byCategory) {
  const ctx = document.getElementById('categoryChart').getContext('2d');
  const labels = Object.keys(byCategory);
  const values = Object.values(byCategory);

  if (categoryChart) categoryChart.destroy();

  categoryChart = new Chart(ctx, {
    type: 'doughnut',
    data: {
      labels,
      datasets: [{
        data: values,
        backgroundColor: ['#667eea', '#f6ad55', '#68d391', '#fc8181', '#63b3ed', '#b794f4']
      }]
    },
    options: { responsive: true }
  });
}