<template>

    <div>
        <Card style="margin-bottom: 8px;padding-bottom: 0">
            <p slot="title">
                <Icon type="search"></Icon>
                监控页面
            </p>
            <Tabs :animated="false">
                <TabPane label="内存使用一览">
                    <div :width="900" style="width:100%;height:500px;zoom: 1;" id="vertical_mem_con"></div>
                </TabPane>
                <TabPane label="交换分区使用一览">
                    <div :width="900" style="width:100%;height:500px;zoom: 1;" id="vertical_swap_con"></div>
                </TabPane>
            </Tabs>

        </Card>
    </div>
</template>

<script>
    import echarts from 'echarts';
    export default {
        name: "vertical",
        data(){
            return {
                timer:'',
                serviceRequestCharts_mem:{},
                serviceRequestCharts_swap:{},
                option_mem : {
                    title : {
                        text: '内存',
                        subtext: '使用一览',
                        x:'center'
                    },
                    tooltip : {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c} ({d}%)"
                    },
                    color:[
                        "#dd2800",
                        "#1800c0"],
                    legend: {
                        orient: 'vertical',
                        left: 'left',
                        data: ['已经使用的内存','未使用的内存']
                    },
                    series : [
                        {
                            name: '内存',
                            type: 'pie',
                            radius : '55%',
                            center: ['50%', '60%'],
                            data:[
                                {value:1, name:'已使用'},
                                {value:1, name:'未使用'},
                            ],
                            itemStyle: {
                                emphasis: {
                                    shadowBlur: 10,
                                    shadowOffsetX: 0,
                                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                                }
                            }
                        }
                    ]
                },
                option_swap : {
                    title : {
                        text: '交换分区',
                        subtext: '使用一览',
                        x:'center'
                    },
                    tooltip : {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c} ({d}%)"
                    },
                    color:[
                        "#d3dd1c",
                        "#4c17c0"],
                    legend: {
                        orient: 'vertical',
                        left: 'left',
                        data: ['已经使用的交换分区','未使用的交换分区']
                    },
                    series : [
                        {
                            name: '内存',
                            type: 'pie',
                            radius : '55%',
                            center: ['50%', '60%'],
                            data:[
                                {value:1, name:'已使用'},
                                {value:1, name:'未使用'},
                            ],
                            itemStyle: {
                                emphasis: {
                                    shadowBlur: 10,
                                    shadowOffsetX: 0,
                                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                                }
                            }
                        },

                    ]
                }
            }
        },
        methods:{
            createChar:function(){
                this.serviceRequestCharts_mem = echarts.init(document.getElementById('vertical_mem_con'));
                this.serviceRequestCharts_mem.setOption(this.option_mem);
                this.serviceRequestCharts_swap = echarts.init(document.getElementById('vertical_swap_con'));
                this.serviceRequestCharts_swap.setOption(this.option_swap);
                //window.addEventListener('resize', function () {
                //this.serviceRequestCharts.resize();
                // });
            },
            createInterval:function () {
                let _this = this;
                this.timer = setInterval(() => {
                    //_this.option.series[0].data[0].value = (Math.random() * 100).toFixed(2) - 0;
                    _this.serviceRequestCharts_mem.setOption(_this.option_mem, true);
                    _this.serviceRequestCharts_swap.setOption(_this.option_swap, true);
                }, 10000)

            },
            getsynData :function(){
                let _this = this;
                this.$http.post('http://localhost:8089/getMemInfo', this.$qs.stringify({
                })).then(function (response) {
                    if(response.data.returnState == "0000") {
                         let useswap = {
                             value:response.data.usedswap,
                             name:'已使用'
                         }
                         let notusewap = {
                             value:response.data.notusedswap,
                             name:'未使用'
                         }
                        _this.option_swap.series[0].data = [];
                        _this.option_swap.series[0].data.push(useswap);
                        _this.option_swap.series[0].data.push(notusewap);
                        _this.option_mem.series[0].data = [];
                        let usesmem = {
                            value:response.data.usedmem,
                            name:'已使用'
                        }
                        let notusesmem = {
                            value:response.data.notusedmem,
                            name:'未使用'
                        }
                        _this.option_mem.series[0].data.push(usesmem);
                        _this.option_mem.series[0].data.push(notusesmem);
                        this.serviceRequestCharts_mem.setOption(this.option_mem);
                        this.serviceRequestCharts_swap.setOption(this.option_swap);
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

<style scoped>

</style>