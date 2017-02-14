var charts = [
    {
        name: "cpu",
        schema: [
            {
                "metadata": {
                    "names": ["Time", "Status", "Millicores"],
                    "types": ["time", "ordinal", "linear"]
                },
                "data": []
            }
        ],
        chartConfig: {
            x: "Time",
            charts: [
                { type: "line", y: "Millicores", color: "Status" }
            ],
            padding: { "top": 30, "left": 60, "bottom": 60, "right": 110 },
            range: true,
            rangeColor: COLOR_BLUE,
            colorScale: [COLOR_GREEN, COLOR_YELLOW, COLOR_BLUE],
            colorDomain: ["Usage Rate", "CPU Limit", "CPU Request"]
        },
        types: [
            { name: TYPE_LANDING, type: TYPE_GET_CPU_DATA }
        ],
        processData: function (data) {
            var result = [];
            for (var i = 0; i < data.length; i++) {
                var series = data[i].series[0];
                var name = series.name;
                var values = series.values;
                for (var j = 0; j < values.length; j++) {
                    var date = new Date(values[j][0]);
                    if(values[j][1] != null){
                        result.push([date.getTime(), name, values[j][1]]);
                    }
                }
            }
            return result;
        }
    },
    {
        name: "memory",
        schema: [
            {
                "metadata": {
                    "names": ["Time", "Status", "MiB"],
                    "types": ["time", "ordinal", "linear"]
                },
                "data": []
            }
        ],
        chartConfig: {
            x: "Time",
            charts: [
                { type: "line", y: "MiB", color: "Status" }
            ],
            padding: { "top": 30, "left": 60, "bottom": 60, "right": 110 },
            range: true,
            rangeColor: COLOR_BLUE,
            colorScale: [COLOR_GREEN, COLOR_YELLOW, COLOR_BLUE, COLOR_RED],
            colorDomain: ["Usage", "Memory Limit", "Memory Request", "Working set"]
        },
        types: [
            { name: TYPE_LANDING, type: TYPE_GET_MEMORY_DATA }
        ],
        processData: function (data) {
            var result = [];
            for (var i = 0; i < data.length; i++) {
                var series = data[i].series[0];
                var name = series.name;
                var values = series.values;
                for (var j = 0; j < values.length; j++) {
                    var date = new Date(values[j][0]);
                    if(values[j][1] != null){
                        result.push([date.getTime(), name, values[j][1]]);
                    }
                }
            }
            return result;
        }
    }
];
