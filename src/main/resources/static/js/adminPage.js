$(() => {
    let mostReadAuthorsData = [];
    let mostReadAuthorsLabels = [];

    // this chart shows all the authors and the number of their books read by all users
    $.ajax({
        url: "/mostReadAuthorsTotal",
        method: "GET",
        success: function (response) {
            response = response.slice(0, 10);
            response.forEach((author) => {
                mostReadAuthorsData.push(author[1]);
                mostReadAuthorsLabels.push(author[0]);
            });
            const ctx = document.getElementById("mostReadAuthorsDoughnutChart");
            new Chart(ctx, {
                type: "doughnut",
                data: {
                    labels: mostReadAuthorsLabels,
                    datasets: [
                        {
                            label: "Número de libros leídos",
                            data: mostReadAuthorsData,
                            backgroundColor: [
                                "#FF6384",
                                "#36A2EB",
                                "#FFCE56",
                                "#4BC0C0",
                                "#9966FF",
                                "#FF9F40",
                                "#28d991",
                                "#3469da",
                                "#ff4d4d",
                                "#ff66b3",
                            ],
                            borderWidth: 1,
                        },
                    ],
                },
                options: {
                    aspectRatio: 2,
                    plugins: {
                        legend: {
                            position: "right",
                        },
                    },
                },
            });
        },
    }).fail(() => {
        $("#mostReadAuthorsChartContainer").append(
            "<p>Lo sentimos, no se pudo cargar la gráfica de los autores más leídos.</p>"
        );
    });

    let mostReadUsersData = [];
    let mostReadUsersLabels = [];

    // this chart shows all the users (top 10) and the number of books they have read
    $.ajax({
        url: "/bestReaders",
        method: "GET",
        success: function (response) {
            response = response.slice(0, 10);
            response.forEach((user) => {
                mostReadUsersData.push(user[1]);
                mostReadUsersLabels.push(user[0]);
            });
            const ctx2 = document.getElementById("mostReadUsersChart");
            new Chart(ctx2, {
                type: "bar",
                data: {
                    labels: mostReadUsersLabels,
                    datasets: [
                        {
                            label: "Número de libros leídos",
                            data: mostReadUsersData,
                            backgroundColor: "#519E8A",
                            borderWidth: 1,
                        },
                    ],
                },
                options: {
                    aspectRatio: 2,
                    scales: {
                        y: {
                            beginAtZero: true,
                            ticks: {
                                callback: function (value, index, values) {
                                    return Math.round(value);
                                },
                            },
                            grid: {
                                display: true,
                            },
                        },
                        x: {
                            grid: {
                                display: false,
                            },
                        },
                    },
                },
            });
        }

    })
        .fail(() => {
            $("#mostReadUsersChartContainer").append(
                "<p>Lo sentimos, no se pudo cargar la gráfica de los usuarios más leídos.</p>"
            );
        });
});