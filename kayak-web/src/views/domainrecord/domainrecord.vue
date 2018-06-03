<style lang="less">
    @import '../../styles/common.less';
    @import '../tables/components/table.less';
</style>
<template>
    <Card style="margin-bottom: 8px;padding-bottom: 0">
        <p slot="title">
            <Icon type="mouse"></Icon>
            域名解析列表
        </p>
        <Row>
            <Form label-position="right" :inline="true" :label-width="80">
                <FormItem label="主机关键字">
                    <Input v-model="querydata.rRKeyWord" clearable placeholder="请输入..."/>
                </FormItem>
                <FormItem label="记录关键字">
                    <Input v-model="querydata.valueKeyWord" clearable placeholder="请输入..."/>
                </FormItem>
                <div style="text-align: center;">
                    <span @click="query" style="margin: 0 10px;"><Button type="primary" icon="search">查询</Button></span>
                    <Button @click="clean" type="ghost">取消</Button>
                </div>
            </Form>
        </Row>
        <Row>
            <div>
                <div style="margin-bottom: 8px;">

                </div>
                <Table :loading="loading" :columns="columns" :data="data"></Table>
                <div style="margin: 10px;overflow: hidden">
                    <div style="float: right;">
                        <Page :showTotal="true" :total="totalCount" :current="pageNumber" :pageSize="pageSize"
                              @on-change="changePage"/>
                    </div>
                </div>
            </div>
        </Row>
        <Row>
            <div>
                <Modal :width="900" v-model="addmodel" @on-visible-change="addchange">
                    <p slot="header" style="text-align:center">
                        <span>添加解析记录</span>
                    </p>
                    <div style="text-align:center">
                        <Form ref="add" :label-width="80" :model="adddata">
                            <FormItem label="主机记录 *">
                                <Input v-model="adddata.rr" placeholder="请输入..."/>
                            </FormItem>
                            <FormItem label="记录类型 *">
                                <Select v-model="adddata.type">
                                    <Option value="A">A记录</Option>
                                    <Option value="NS">NS记录</Option>
                                    <Option value="MX">MX记录</Option>
                                    <Option value="TXT">TXT记录</Option>
                                    <Option value="CNAME">CNAME记录</Option>
                                    <Option value="SRV">SRV记录</Option>
                                    <Option value="AAAA">AAAA记录</Option>
                                    <Option value="CAA">CAA记录</Option>
                                    <Option value="REDIRECT_URL">显性URL转发</Option>
                                    <Option value="FORWARD_URL">隐性URL转发</Option>
                                </Select>
                            </FormItem>
                            <FormItem label="记录值">
                                <Input v-model="adddata.value" placeholder="请输入..."/>
                            </FormItem>
                            <FormItem label="生存时间">
                                <Input v-model="adddata.ttl" placeholder="请输入..."/>
                            </FormItem>
                            <FormItem label="优先级">
                                <Input v-model="adddata.priority" placeholder="请输入..."/>
                            </FormItem>
                        </Form>
                    </div>
                    <div slot="footer" style="text-align: center;">
                        <Button type="primary" @click="add">添加</Button>
                        <Button @click="cleanadd">取消</Button>
                    </div>
                </Modal>
            </div>
        </Row>
        <Row>
            <div>
                <Modal :width="900" v-model="modmodel" >
                    <p slot="header" style="text-align:center">
                        <span>修改解析记录</span>
                    </p>
                    <div style="text-align:center">
                        <Form ref="mod" :label-width="80" :model="moddata">
                            <FormItem label="ID *">
                                <Input v-model="moddata.recordId" :readonly=true />
                            </FormItem>
                            <FormItem label="主机记录 *">
                                <Input v-model="moddata.rr" placeholder="请输入..."/>
                            </FormItem>
                            <FormItem label="记录类型 *">
                                <Select v-model="moddata.type">
                                    <Option value="A">A记录</Option>
                                    <Option value="NS">NS记录</Option>
                                    <Option value="MX">MX记录</Option>
                                    <Option value="TXT">TXT记录</Option>
                                    <Option value="CNAME">CNAME记录</Option>
                                    <Option value="SRV">SRV记录</Option>
                                    <Option value="AAAA">AAAA记录</Option>
                                    <Option value="CAA">CAA记录</Option>
                                    <Option value="REDIRECT_URL">显性URL转发</Option>
                                    <Option value="FORWARD_URL">隐性URL转发</Option>
                                </Select>
                            </FormItem>
                            <FormItem label="记录值">
                                <Input v-model="moddata.value" placeholder="请输入..."/>
                            </FormItem>
                            <FormItem label="生存时间">
                                <Input v-model="moddata.ttl" placeholder="请输入..."/>
                            </FormItem>
                            <FormItem label="优先级">
                                <Input v-model="moddata.priority" placeholder="请输入..."/>
                            </FormItem>
                        </Form>
                    </div>
                    <div slot="footer" style="text-align: center;">
                        <Button type="primary" @click="mod">修改</Button>
                        <Button @click="cleanmod">取消</Button>
                    </div>
                </Modal>
            </div>
        </Row>
        <Row>
            <div>
                <Modal :width="900" v-model="infomodel" >
                    <p slot="header" style="text-align:center">
                        <span>详细信息</span>
                    </p>
                    <div style="text-align:center">
                        <Form ref="mod" :label-width="80" :model="infodata">
                            <FormItem label="ID">
                                <Input v-model="infodata.recordId" :readonly=true />
                            </FormItem>
                            <FormItem label="域名名称">
                                <Input v-model="infodata.domainName" :readonly=true />
                            </FormItem>
                            <FormItem label="解析类型">
                                <Input v-model="infodata.rr" />
                            </FormItem>
                            <FormItem label="记录类型">
                                <Select v-model="infodata.type">
                                    <Option value="A">A记录</Option>
                                    <Option value="NS">NS记录</Option>
                                    <Option value="MX">MX记录</Option>
                                    <Option value="TXT">TXT记录</Option>
                                    <Option value="CNAME">CNAME记录</Option>
                                    <Option value="SRV">SRV记录</Option>
                                    <Option value="AAAA">AAAA记录</Option>
                                    <Option value="CAA">CAA记录</Option>
                                    <Option value="REDIRECT_URL">显性URL转发</Option>
                                    <Option value="FORWARD_URL">隐性URL转发</Option>
                                </Select>
                            </FormItem>
                            <FormItem label="记录值">
                                <Input v-model="infodata.value" />
                            </FormItem>
                            <FormItem label="生存时间">
                                <Input v-model="infodata.ttl" />
                            </FormItem>
                            <FormItem label="MX记录的优先级">
                                <Input v-model="infodata.priority" />
                            </FormItem>
                            <FormItem label="解析线路">
                                <Input v-model="infodata.line" />
                            </FormItem>
                            <FormItem label="解析记录状态">
                                <Input v-model="infodata.status" />
                            </FormItem>
                            <FormItem label="锁定状态">
                                <Input v-model="infodata.locked" />
                            </FormItem>
                        </Form>
                    </div>
                    <div slot="footer" style="text-align: center;">
                        <Button type="primary" @click="infoclean">关闭</Button>
                    </div>
                </Modal>
            </div>
        </Row>
    </Card>
</template>
<script>
    export default {
        name: "domainlog",
        data() {
            return {
                loading: true,
                totalCount: 0,
                pageNumber: 1,
                pageSize: 20,
                data: [],
                columns: [
                    {
                        key: 'rr',
                        title: '主机',
                        sortable: true
                    },
                    {
                        key: 'value',
                        title: '值',
                        sortable: true
                    },
                    {
                        key: 'status',
                        title: '记录状态',
                        sortable: true
                    },
                    {
                        title: '操作',
                        key: 'action',
                        width: 250,
                        align: 'center',
                        render: (h, params) => {
                            return h('div', [
                                h('strong', {
                                    style: {
                                        marginRight: '5px'
                                    },
                                }, '记录状态'),
                                h('i-switch', {
                                    props: {
                                        type: 'primary',
                                        size: 'small',
                                        value: params.row.status === 'ENABLE'
                                    },
                                    style: {
                                        marginRight: '5px'
                                    },
                                    on: {
                                        'on-change': (value) => {
                                            this.setstatus(params.row,value);

                                        }
                                    }
                                }, '开关'),
                                h('Button', {
                                    props: {
                                        type: 'info',
                                        size: 'small'
                                    },
                                    style: {
                                        marginRight: '5px'
                                    },
                                    on: {
                                        click: () => {
                                            this.infoobj(params.row)
                                        }
                                    }
                                }, '详细'),
                                h('Button', {
                                    props: {
                                        type: 'primary',
                                        size: 'small'
                                    },
                                    style: {
                                        marginRight: '5px'
                                    },
                                    on: {
                                        click: () => {
                                            this.showmodpage(params.row);
                                        }
                                    }
                                }, '修改'),
                                h('Button', {
                                    props: {
                                        type: 'error',
                                        size: 'small'
                                    },
                                    on: {
                                        click: () => {
                                            this.delete(params.row);
                                        }
                                    }
                                }, '删除')
                            ]);
                        }
                    }
                ],
                querydata:{
                    rRKeyWord:'',
                    valueKeyWord:''

                },
                addmodel: false,
                adddata:{
                    rr:'',
                    type:'',
                    value:'',
                    ttl:'',
                    priority:''
                },
                modmodel:false,
                moddata:{
                    recordId:'',
                    rr:'',
                    type:'',
                    value:'',
                    ttl:'',
                    priority:''
                },
                infomodel:false,
                infodata:{
                    requestId:'',
                    domainId:'',
                    domainName:'',
                    punyCode:'',
                    groupId:'',
                    groupName:'',
                    recordId:'',
                    rr:'',
                    type:'',
                    value:'',
                    ttl:'',
                    priority:'',
                    line:'',
                    status:'',
                    locked:''
                }
            };
        },
        methods: {
            query() {
                let _this = this;
                this.$http.post(this.httpurl.toString() + '/aliyun/describeDomainRecords', this.$qs.stringify({
                    pageNumber: this.pageNumber,
                    pageSize: this.pageSize,
                    domainName: this.querydata.domainName,
                    rRKeyWord:this.querydata.rRKeyWord,
                    typeKeyWord:this.querydata.typeKeyWord,
                    valueKeyWord:this.querydata.valueKeyWord
                })).then(function (response) {
                    _this.pageNumber = response.data.pageNumber;
                    _this.data = response.data.domainRecords;
                    _this.totalCount = response.data.totalCount;
                    _this.pageSize = response.data.pageSize;
                    _this.loading = false;
                }).catch(function (error) {
                    if(typeof(error.response) == "undefined"){
                        _this.$Message.error("错误信息：" + error);
                    }else{
                        _this.$Message.error("错误信息：" + error.response.data.message);
                    }
                    console.log(error);
                });
            },
            changePage(pageNumber) {
                this.pageNumber = pageNumber;
                this.query();
            },clean(){
                this.querydata.rRKeyWord = '';
                this.querydata.typeKeyWord = '';
                this.querydata.valueKeyWord ='';
            },showaddpage(){
                this.addmodel = true;
            },addchange(){
                this.adddata = {};
            },cleanadd(){
                this.adddata = {};
            },add(){
                let _this = this;
                this.$http.post(this.httpurl.toString() + '/aliyun/addDomainRecord', this.$qs.stringify({
                    rr:this.adddata.rr,
                    type:this.adddata.type,
                    value:this.adddata.value,
                    ttl:this.adddata.ttl,
                    priority:this.adddata.priority
                })).then(function (response) {
                    _this.addmodel = false;
                    _this.query();
                }).catch(function (error) {
                    if(typeof(error.response) == "undefined"){
                        _this.$Message.error("错误信息：" + error);
                    }else{
                        _this.$Message.error("错误信息：" + error.response.data.message);
                    }
                });
            },delete(row){
                let _this = this;
                this.$http.post(this.httpurl.toString() + '/aliyun/deleteDomainRecord', this.$qs.stringify({
                    recordId:row.recordId
                })).then(function (response) {
                    _this.query();
                }).catch(function (error) {
                    if(typeof(error.response) == "undefined"){
                        _this.$Message.error("错误信息：" + error);
                    }else{
                        _this.$Message.error("错误信息：" + error.response.data.message);
                    }
                });
            },showmodpage(row){
                this.modmodel = true;
                this.moddata.recordId = row.recordId;
                this.moddata.rr = row.rr;
                this.moddata.type = row.type;
                this.moddata.value = row.value;
                this.moddata.ttl = row.ttl;
                this.moddata.priority = row.priority;
            },cleanmod(){
                this.modmodel = false;
            },mod(){
                let _this = this;
                this.$http.post(this.httpurl.toString() + '/aliyun/updateDomainRecord', this.$qs.stringify({
                    recordId:this.moddata.recordId,
                    rr:this.moddata.rr,
                    type:this.moddata.type,
                    value:this.moddata.value,
                    ttl:this.moddata.ttl,
                    priority:this.moddata.priority
                })).then(function (response) {
                    _this.modmodel = false;
                    _this.query();
                }).catch(function (error) {
                    if(typeof(error.response) == "undefined"){
                        _this.$Message.error("错误信息：" + error);
                    }else{
                        _this.$Message.error("错误信息：" + error.response.data.message);
                    }
                });
            },setstatus(row,value){
                let _this = this;
                if(value === false){
                    row.status = 'DISABLE';
                }else{
                    row.status = 'ENABLE';
                }
                this.$http.post(this.httpurl.toString() + '/aliyun/setDomainRecordStatus', this.$qs.stringify({
                    recordId:row.recordId,
                    status:row.status
                })).then(function (response) {
                    _this.query();
                }).catch(function (error) {
                    if(typeof(error.response) == "undefined"){
                        _this.$Message.error("错误信息：" + error);
                    }else{
                        _this.$Message.error("错误信息：" + error.response.data.message);
                    }
                });
            },infoobj(row){
                let _this = this;
                this.$http.post(this.httpurl.toString() + '/aliyun/describeDomainRecordInfo', this.$qs.stringify({
                    recordId:row.recordId
                })).then(function (response) {
                    _this.infodata.requestId = response.data.requestId;
                    _this.infodata.domainId= response.data.domainId;
                    _this.infodata.domainName= response.data.domainName;
                    _this.infodata.punyCode= response.data.punyCode;
                    _this.infodata.groupId= response.data.groupId;
                    _this.infodata.groupName= response.data.groupName;
                    _this.infodata.recordId= response.data.recordId;
                    _this.infodata.rr = response.data.rr;
                    _this.infodata.type= response.data.type;
                    _this.infodata.value= response.data.value;
                    _this.infodata.ttl= response.data.ttl;
                    _this.infodata.priority= response.data.priority;
                    _this.infodata.line= response.data.line;
                    _this.infodata.status= response.data.status;
                    _this.infodata.locked= response.data.locked.toString();
                    _this.infomodel = true;
                }).catch(function (error) {
                    if(typeof(error.response) == "undefined"){
                        _this.$Message.error("错误信息：" + error);
                    }else{
                        _this.$Message.error("错误信息：" + error.response.data.message);
                    }
                });
            },infoclean(){
                this.infomodel = false;
            }
        },
        mounted() {
            this.query();
        }
    }
</script>