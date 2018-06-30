<style lang="less">

</style>
<template>
    <Card style="margin-bottom: 8px;padding-bottom: 0">
        <div>
            <p slot="title">
                <Icon type="mouse"></Icon>
                复制控制列表
            </p>
            <Row>
                <Form label-position="right" :inline="true" :label-width="80">
                    <div style="text-align: center;">
                        <Button type="primary" icon="search" @click="query">查询</Button>
                    </div>
                </Form>
            </Row>
            <Row>
                <div>
                    <div style="margin-bottom: 8px;">
                        <Button type="primary" icon="plus" @click="addk8s=true">添加RC操作</Button>
                    </div>
                    <Table :loading="loading" :columns="columns" :data="data"></Table>
                    <div style="margin: 10px;overflow: hidden">
                        <div style="float: right;">
                            <Page :showTotal="true" :total="totalCount" :current="pageNumber" :pageSize="pageSize" @on-change="changePage"></Page>
                        </div>
                    </div>
                </div>
            </Row>
        </div>
        <div>
            <Modal :width="800" v-model="addk8s" >
                <p slot="header" style="text-align:center">
                    <span>新增RC操作</span>
                </p>
                <codemirror v-model="k8scode" :options="addOptions"></codemirror>
                <div slot="footer" style="text-align: center;">
                    <Button type="primary" @click="saveK8s">保存</Button>
                    <Button type="ghost" @click="k8scode=''">重置</Button>
                </div>
            </Modal>
        </div>
        <div>
            <Modal :width="800"  v-model="infok8s" >
                <p slot="header" style="text-align:center">
                    <span>RC详情</span>
                </p>
                <codemirror v-model="infok8scode" :options="editOptions" ></codemirror>
                <div slot="footer" style="text-align: center;">
                    <Button type="primary" @click="infok8s=false">关闭</Button>
                </div>
            </Modal>
        </div>
    </Card>
</template>
<script>

    import { codemirror } from 'vue-codemirror'
    require("codemirror/mode/python/python.js");
    require('codemirror/addon/fold/foldcode.js');
    require('codemirror/addon/fold/foldgutter.js');
    require('codemirror/addon/fold/brace-fold.js');
    require('codemirror/addon/fold/xml-fold.js');
    require('codemirror/addon/fold/indent-fold.js');
    require('codemirror/addon/fold/markdown-fold.js');
    require('codemirror/addon/fold/comment-fold.js');
    import 'codemirror/lib/codemirror.css'
    export default {
        name: "k8s",
        components: {
            codemirror
        },
        data() {
            return {
                loading: true,
                totalCount: 0,
                pageNumber: 1,
                pageSize: 20,
                addk8s:false,
                infok8s:false,
                data: [],
                k8scode:'',
                infok8scode:'',
                columns: [
                    {
                        key: 'kind',
                        title: '类型',
                        sortable: true,
                    },
                    {
                        key: 'name',
                        title: '容器名称',
                        sortable: true,
                    },
                    {
                        key: 'namespace',
                        title: '容器空间',
                        sortable: true,
                    },
                    {
                        key: 'replicas',
                        title: '副本',
                        sortable: true,
                    },
                    {
                        key: 'ports',
                        title: '已用端口',
                        sortable: true,
                    },
                    {
                        key: 'generation',
                        title: '存活',
                        sortable: true,
                    },
                    {
                        key: 'creationTimestamp',
                        title: '创建时间',
                        sortable: true,
                    },
                    {
                        title: '操作',
                        key: 'action',
                        width: 200,
                        align: 'center',
                        render: (h, params) => {
                            return h('div', [
                                h('Poptip', {
                                    props: {
                                        title: '确认删除吗？',
                                        confirm:true
                                    },
                                    on: {
                                        "on-cancel": () => {
                                        },
                                        "on-ok": () => {
                                            this.deletePage(params.row);
                                        }
                                    }
                                }, [h('Button', {
                                    props: {
                                        type: 'error',
                                        size: 'small'
                                    }
                                }, '删除')]),
                                h('Button', {
                                    props: {
                                        type: 'info',
                                        size: 'small'
                                    },
                                    on: {
                                        click: () => {
                                            this.showinfoPage(params.row);
                                        }
                                    }
                                }, '详细')
                            ]);
                        }
                    }
                ],
                addOptions: {
                    mode: 'text/javascript',
                    theme: 'duotone-dark',
                    lineNumbers: true,
                    line: true,
                },
                editOptions: {
                    mode: 'text/javascript',
                    theme: 'duotone-dark',
                    lineNumbers: true,
                    line: true,
                    readOnly:true
                },
            };
        },
        methods: {
            query: function () {

                let _this = this;
                this.kayak.httpUtil.query({url:"k8s/getReplicationControllerLists.json",method:"post",params:{'pageNumber':this.pageNumber, 'pageSize':this.pageSize},successAlert:false}).then(data=>{
                    _this.totalCount = data.returndata.totalCount;
                    _this.data = data.returndata.rows;
                    _this.loading = false;
                });
            },
            changePage: function (pageNumber) {
                this.pageNumber = pageNumber;
                this.query();
            },
            saveK8s:function () {
                let _this = this;
                this.kayak.httpUtil.update({url:"k8s/createReplicationControlleredit.json",method:"post",params:{'k8scode':this.k8scode},successAlert:false}).then(data=>{
                    _this.addk8s = false;
                    _this.k8scode = '';
                    _this.query();
                });
            },
            showinfoPage:function (row) {
                this.infok8scode = row.json;
                this.infok8s = true;
            },
            deletePage:function (row) {
                let _this = this;
                this.kayak.httpUtil.update({url:"k8s/deleteReplicationControlleredit.json",method:"post",params:{'k8scode':JSON.stringify(row.replicationcontroller)},successAlert:false}).then(data=>{
                    _this.addk8s = false;
                    _this.query();
                });
            }

        },
        mounted() {
            this.query();
        }
    }
</script>