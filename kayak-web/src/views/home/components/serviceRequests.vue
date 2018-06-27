<template>
    <div style="width:100%;height:100%;" id="service_request_con"></div>
</template>

<script>
    import echarts from 'echarts';
    export default {
        name: 'serviceRequests',
        data(){
            return {
                timer:'',
                serviceRequestCharts:{},
                option :{
                    tooltip: {
                        trigger: 'axis'
                    },
                    legend: {
                        data:[]
                    },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        containLabel: true
                    },
                    toolbox: {
                        feature: {
                            saveAsImage: {}
                        }
                    },
                    xAxis: {
                        type: 'category',
                        boundaryGap: false,
                        data: []
                    },
                    yAxis: {
                        type: 'value'
                    },
                    series: [

                    ]
                },
            }
        },
        methods:{
            createChar:function(){
                this.serviceRequestCharts = echarts.init(document.getElementById('service_request_con'));
                this.serviceRequestCharts.setOption(this.option);
            },
            createInterval:function () {
                this.timer = setInterval(() => {
                    this.getsynData();
                }, 10000)

            },
            getsynData :function(){
                let _this = this;
                this.kayak.httpUtil.query({url:"monitor/querymonitor.json",method:"post",successAlert:false}).then(data=>{
                    if(data.success == true) {
                        _this.option.legend.data = data.legendData;
                        _this.option.xAxis.data = data.xAxisdata;
                        _this.option.series = [];
                        for(var i = 0;i<data.data.length;i++){
                            let series = {
                                name: data.data[i].name,
                                type:'line',
                                stack: '交易量',
                                areaStyle: {normal: {}},
                                data:data.data[i].seriesdata
                            }
                            _this.option.series.push(series);
                        }
                        _this.serviceRequestCharts.setOption(_this.option,true);
                    }else{
                        _this.$Message.error(data.error);
                    }
                });
            }
        },
        mounted () {
            this.createChar();
            this.createInterval();
        }
    };
</script>