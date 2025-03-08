$(document).ready(function () {
    // Load initial revenue data
    loadRevenueData();

    // Event listener for time range change
    $("#summaryTimeRange").on("change", function () {
        loadRevenueData();
    });

    // Function to load revenue data
    function loadRevenueData() {
        let timeRange = $("#summaryTimeRange").val() || "today"; // Default to "today"

        console.log("Loading revenue data with timeRange: " + timeRange);

        $.ajax({
            url: "/ClothingShop/revenue",
            type: "GET",
            data: {timeRange: timeRange},
            beforeSend: function () {
                $("#orders .card-title").text("Loading...");
                $("#soldProducts .card-title").text("Loading...");
                $("#revenueSummary .card-title").text("Loading...");
            },
            success: function (data) {
                // Parse the response and update each card
                let parser = new DOMParser();
                let doc = parser.parseFromString(data, "text/html");

                let ordersContent = doc.getElementById("ordersContent");
                let soldProductsContent = doc.getElementById("soldProductsContent");
                let revenueSummaryContent = doc.getElementById("revenueSummaryContent");

                if (ordersContent) {
                    $("#orders").html(ordersContent.innerHTML);
                }
                if (soldProductsContent) {
                    $("#soldProducts").html(soldProductsContent.innerHTML);
                }
                if (revenueSummaryContent) {
                    $("#revenueSummary").html(revenueSummaryContent.innerHTML);
                }
            },
            error: function (xhr, status, error) {
                console.error("Error loading revenue data:", error, "Status:", xhr.status, "Response:", xhr.responseText);
                $("#orders .card-title").text("Error");
                $("#soldProducts .card-title").text("Error");
                $("#revenueSummary .card-title").text("Error");
            }
        });
    }
});

var ctx = document.getElementById("revenueChart").getContext("2d");
var revenueChart = new Chart(ctx, {
    type: "bar",
    data: {
        labels: ["Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5"],
        datasets: [
            {
                label: "Revenue (VNĐ)",
                data: [1000000, 1200000, 800000, 1500000, 2000000],
                backgroundColor: "rgba(54, 162, 235, 0.6)",
                borderColor: "rgba(54, 162, 235, 1)",
                borderWidth: 1
            }
        ]
    },
    options: {
        scales: {
            y: {
                beginAtZero: true,
                title: {
                    display: true,
                    text: "VNĐ"
                }
            }
        }
    }
});

// Function to load chart data
function loadChartData(timeRange) {
    console.log("Loading chart data with timeRange: " + timeRange);
    $.ajax({
        url: "/ClothingShop/revenue",
        type: "GET",
        data: {chart: "true", timeRange: timeRange},
        dataType: "html",
        success: function (response) {
            console.log("Chart data response:", response);
            // Parse the HTML response
            var $response = $(response);
            var labels = $response.find("#labels .label").map(function () {
                return $(this).text();
            }).get();
            var data = $response.find("#values .value").map(function () {
                return parseFloat($(this).text()) || 0; // Default to 0 if parsing fails
            }).get();

            console.log("Parsed labels:", labels);
            console.log("Parsed data:", data);

            // Update the chart with new data
            revenueChart.data.labels = labels;
            revenueChart.data.datasets[0].data = data;
            revenueChart.update();
        },
        error: function (xhr, status, error) {
            console.error("Error loading chart data:", error, "Status:", xhr.status, "Response:", xhr.responseText);
            alert("Failed to load chart data. Please try again. Status: " + xhr.status + ", Response: " + xhr.responseText);
        }
    });
}

// Event listener for chart time range change
$("#chartTimeRange").on("change", function () {
    var timeRange = $(this).val();
    if (timeRange) {
        loadChartData(timeRange);
    }
});

// Initial load for chart (default to "thisMonths")
loadChartData("thisMonths");