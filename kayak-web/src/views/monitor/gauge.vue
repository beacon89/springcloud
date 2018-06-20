<template>
    <div>
        <Card style="margin-bottom: 8px;padding-bottom: 0">
            <p slot="title">
                <Icon type="search"></Icon>
                监控页面
            </p>
            <Tabs :animated="false">
                <TabPane label="CPU使用率">
                    <div :width="900" style="width:100%;height:500px;zoom: 1;" id="gauge_cpu_con"></div>
                </TabPane>
                <TabPane label="各CPU使用情况">
                    <div :width="900" style="width:100%;height:500px;zoom: 1;" id="gauge_allcpu_con"></div>
                </TabPane>
            </Tabs>

        </Card>
    </div>
</template>

<script>
    import echarts from 'echarts';
    export default {
        name: "gauge",
        data(){
            return {
                timer:'',
                serviceRequestChartscpu:{},
                serviceRequestChartsallcpu:{},
                option_cpu :{
                    tooltip : {
                        formatter: "{a} <br/>{b} : {c}%"
                    },
                    toolbox: {
                        feature: {
                            restore: {},
                            saveAsImage: {}
                        }
                    },
                    series: [
                        {
                            name: 'CUP使用率',
                            type: 'gauge',
                            detail: {formatter:'{value}%'},
                            data: [{value: 50, name: 'CUP使用率'}]
                        }
                    ]
                },
                option_all :{
                    tooltip : {
                        trigger: 'axis',
                        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                        }
                    },
                    legend: {
                        data: []
                    },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        containLabel: true
                    },
                    xAxis:  {
                        type: 'value'
                    },
                    yAxis: {
                        type: 'category',
                        data: []
                    },
                    series: [
                        {
                            name: '123',
                            type: 'bar',
                            stack: '总量',
                            label: {
                                normal: {
                                    show: true,
                                    position: 'insideRight'
                                }
                            },
                            data: [50, 50, 50, 50, 50]
                        },
                        {
                            name: '456',
                            type: 'bar',
                            stack: '总量',
                            label: {
                                normal: {
                                    show: true,
                                    position: 'insideRight'
                                }
                            },
                            data:[50, 50, 50, 50, 50]
                        },

                    ]
                },
            }
        },
        methods:{
            createChar:function(){
                this.serviceRequestChartscpu = echarts.init(document.getElementById('gauge_cpu_con'));
                this.serviceRequestChartscpu.setOption(this.option_cpu);
                this.serviceRequestChartsallcpu = echarts.init(document.getElementById('gauge_allcpu_con'));
                this.serviceRequestChartsallcpu.setOption(this.option_all);

                //window.addEventListener('resize', function () {
                //this.serviceRequestCharts.resize();
                // });
            },
            createInterval:function () {
                let _this = this;
                this.timer = setInterval(() => {
                    _this.serviceRequestChartscpu.setOption(_this.option_cpu, true);
                    _this.serviceRequestChartsallcpu.setOption(_this.option_all, true);
                }, 10000)

            },
            getsynData :function(){
                let _this = this;
                this.$http.post('http://localhost:8080/getCpuInfo', this.$qs.stringify({
                })).then(function (response) {
                    if(response.data.returnState == "0000") {
                        _this.option_cpu.series[0].data = [];
                        _this.option_cpu.series[0].data[0].value = response.data.cpuRation;
                        _this.option_all.legend = [];
                        _this.option_all.yAxis.data
                        _this.option_all.series[0].data
                        _this.option.xAxis = [];
                        _this.option.xAxis.push(xAxisobj);
                        _this.option.series = [];
                        _this.option.series.push(seriesobj);
                        _this.serviceRequestCharts.setOption(_this.option);
                        //_this.serviceRequestCharts.resize();
                    }else{
                        _this.$Message.error(response.data.returnMsg);
                    }
                }).catch(function (error) {
                    if(typeof(error.response) == "undefined"){
                        _this.$Message.error("错误信息：" + error);
                    }else{
                        _this.$Message.error("错误信息：" + error.response.data.message);
                    }
                });


            }
        },
        mounted () {

            this.createChar();

            this.createInterval();
        }
    }
</script>

