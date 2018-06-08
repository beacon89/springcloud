<template>
    <div>
        <Card style="margin-bottom: 8px;padding-bottom: 0">
            <p slot="title">
                <Icon type="search"></Icon>
                监控页面
            </p>
            <div :width="900" style="width:100%;height:500px;zoom: 1;" id="gauge_request_con"></div>
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
                serviceRequestCharts:{},
                option :{
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
            }
        },
        methods:{
            createChar:function(){
                this.serviceRequestCharts = echarts.init(document.getElementById('gauge_request_con'));
                this.serviceRequestCharts.setOption(this.option);
                //window.addEventListener('resize', function () {
                //this.serviceRequestCharts.resize();
                // });
            },
            createInterval:function () {
                let _this = this;
                this.timer = setInterval(() => {
                    _this.option.series[0].data[0].value = (Math.random() * 100).toFixed(2) - 0;
                    _this.serviceRequestCharts.setOption(_this.option, true);
                }, 10000)

            },
            getsynData :function(){
                let _this = this;
                this.$http.post('http://localhost:8089/querymonitor', this.$qs.stringify({
                })).then(function (response) {
                    if(response.data.returnState == "0000") {
                        let xAxisobj = {
                            type : 'category',
                            boundaryGap:false,
                            data:response.data.xAxisdata
                        }
                        let seriesobj = {
                            name: '流量监控',
                            type: 'line',
                            stack: '总量',
                            areaStyle: {normal: {color: '#2d8cf0'}},
                            data: response.data.seriesdata
                        }
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

