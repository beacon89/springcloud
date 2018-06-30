
<template>
    <Card style="margin-bottom: 8px;padding-bottom: 0">
        <p slot="title">
            <Icon type="mouse"></Icon>
            域名解析列表
        </p>
        <Row>
            <Form ref="querydata" label-position="right" :model="querydata" :inline="true" :label-width="120">

                <Row>
                    <Col span="8" v-if="userroletype ==='1'">
                        <FormItem label="主机名称关键字" prop="rRKeyWord">
                            <Input v-model="querydata.rRKeyWord" clearable placeholder="请输入..."/>
                        </FormItem>
                    </Col>
                    <!--
                    <Col span="8">
                        <FormItem label="主机IP关键字" prop="valueKeyWord">
                            <Input v-model="querydata.valueKeyWord" clearable placeholder="请输入..."/>
                        </FormItem>
                    </Col>-->
                </Row>
                <div style="text-align: center;">
                    <Button type="primary" icon="search" @click="query('querydata')">查询</Button>
                    <Button @click="clean('querydata')" type="ghost">取消</Button>
                </div>
            </Form>
        </Row>
        <Row>
            <div>
                <div style="margin-bottom: 8px;">
                   <!-- <Button type="primary" icon="plus" @click="showaddpage('adddata')">添加域名</Button>-->
                </div>
                <Table :loading="loading" :columns="columns" :data="data"></Table>
                <div style="margin: 10px;overflow: hidden">
                    <div style="float: right;">
                        <Page :showTotal="true" :total="totalCount" :current="pageNumber" :pageSize="pageSize" @on-change="changePage"/>
                    </div>
                </div>
            </div>
        </Row>
        <Row>
            <div>
                <Modal :width="900" v-model="addmodel" >
                    <p slot="header" style="text-align:center">
                        <span>添加解析记录</span>
                    </p>
                    <div style="text-align:center">
                        <Form ref="adddata" :label-width="80" :model="adddata" :rules="addValidate">
                            <FormItem label="主机名称" prop="rr">
                                <Row>
                                    <Col span="12">
                                        <Input v-model="adddata.rr" clearable placeholder="请输入..."/>
                                    </Col>
                                </Row>
                            </FormItem>
                            <FormItem label="主机类型" prop="type">
                                <Row>
                                    <Col span="12">
                                        <Select v-model="adddata.type" clearable>
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
                                    </Col>
                                </Row>
                            </FormItem>
                            <FormItem label="主机IP" prop="value">
                                <Row>
                                    <Col span="12">
                                        <Input v-model="adddata.value" clearable placeholder="请输入..."/>
                                    </Col>
                                </Row>
                            </FormItem>
                            <FormItem label="生存时间" prop="ttl">
                                <Row>
                                    <Col span="12" >
                                        <Input v-model="adddata.ttl" clearable placeholder="请输入..."/>
                                    </Col>
                                </Row>
                            </FormItem>
                            <FormItem label="优先级" prop="priority">
                                <Row>
                                    <Col span="12" >
                                        <Input v-model="adddata.priority" clearable placeholder="请输入..."/>
                                    </Col>
                                </Row>
                            </FormItem>
                        </Form>
                    </div>
                    <div slot="footer" style="text-align: center;">
                        <Button type="primary" @click="add('adddata')">添加</Button>
                        <Button @click="cleanadd('adddata')">取消</Button>
                    </div>
                </Modal>
            </div>
        </Row>
        <Row>
            <div>
                <Modal :width="800" v-model="modmodel" >
                    <p slot="header" style="text-align:center">
                        <span>修改解析记录</span>
                    </p>
                    <div style="text-align:center">
                        <Form ref="moddata" :label-width="80" :model="moddata" :rules="modValidate">
                            <FormItem label="ID" >
                                <Row>
                                    <Col span="12">
                                        <Input v-model="moddata.recordId" disabled />
                                    </Col>
                                </Row>
                            </FormItem>
                            <FormItem label="主机名称" prop="rr">
                                <Row>
                                    <Col span="12">
                                        <Input v-model="moddata.rr" clearable/>
                                    </Col>
                                </Row>
                            </FormItem>
                            <FormItem label="主机类型" prop="type">
                                <Row>
                                    <Col span="12">
                                        <Select v-model="moddata.type" clearable>
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
                                    </Col>
                                </Row>
                            </FormItem>
                            <FormItem label="主机IP" prop="value">
                                <Row>
                                    <Col span="12">
                                        <Input v-model="moddata.value" placeholder="请输入..." clearable/>
                                    </Col>
                                </Row>
                            </FormItem>
                            <FormItem label="生存时间" prop="ttl">
                                <Row>
                                    <Col span="12">
                                        <Input v-model="moddata.ttl" placeholder="请输入..." clearable/>
                                    </Col>
                                </Row>
                            </FormItem>
                            <FormItem label="优先级" prop="priority">
                                <Row>
                                    <Col span="12">
                                        <Input v-model="moddata.priority" placeholder="请输入..." clearable/>
                                    </Col>
                                </Row>
                            </FormItem>
                        </Form>
                    </div>
                    <div slot="footer" style="text-align: center;">
                        <Button type="primary" @click="mod('moddata')">修改</Button>
                        <Button @click="cleanmod('moddata')">取消</Button>
                    </div>
                </Modal>
            </div>
        </Row>
        <Row>
            <div>
                <Modal :width="800" v-model="infomodel" >
                    <p slot="header" style="text-align:center">
                        <span>详细信息</span>
                    </p>
                    <div style="text-align:center">
                        <Form ref="mod" :label-width="120" label-position="left" :model="infodata" >
                            <FormItem label="ID">
                                <Row>
                                    <Col span="12">
                                        <Input v-model="infodata.recordId" disabled/>
                                    </Col>
                                </Row>
                            </FormItem>
                            <FormItem label="域名名称">
                                <Row>
                                    <Col span="12">
                                        <Input v-model="infodata.domainName" disabled />
                                    </Col>
                                </Row>
                            </FormItem>
                            <FormItem label="主机名称">
                                <Row>
                                    <Col span="12">
                                        <Input v-model="infodata.rr" disabled/>
                                    </Col>
                                </Row>
                            </FormItem>
                            <FormItem label="主机类型">
                                <Row>
                                    <Col span="12">
                                        <Select v-model="infodata.type" disabled>
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
                                    </Col>
                                </Row>
                            </FormItem>
                            <FormItem label="主机IP">
                                <Row>
                                    <Col span="12">
                                        <Input v-model="infodata.value" disabled/>
                                    </Col>
                                </Row>
                            </FormItem>
                            <FormItem label="生存时间">
                                <Row>
                                    <Col span="12">
                                        <Input v-model="infodata.ttl" disabled/>
                                    </Col>
                                </Row>
                            </FormItem>
                            <FormItem label="MX主机优先级">
                                <Row>
                                    <Col span="12">
                                        <Input v-model="infodata.priority" disabled/>
                                    </Col>
                                </Row>
                            </FormItem>
                            <FormItem label="解析线路">
                                <Row>
                                    <Col span="12">
                                        <Input v-model="infodata.line" disabled/>
                                    </Col>
                                </Row>
                            </FormItem>
                            <FormItem label="解析记录状态">
                                <Row>
                                    <Col span="12">
                                        <Input v-model="infodata.status" disabled/>
                                    </Col>
                                </Row>
                            </FormItem>
                            <FormItem label="锁定状态">
                                <Row>
                                    <Col span="12">
                                        <Input v-model="infodata.locked" disabled/>
                                    </Col>
                                </Row>
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

            const validateIP = (rule, value, callback) => {
                var reg = /((25[0-5]|2[0-4]\d|((1\d{2})|([1-9]?\d)))\.){3}(25[0-5]|2[0-4]\d|((1\d{2})|([1-9]?\d)))/;
                if(!reg.test(value) && value !== ""){
                    callback(new Error('请填写正确的IP地址!!!!!'));
                }else{
                    callback();
                }
            };

            return {
                loading: true,
                totalCount: 0,
                pageNumber: 1,
                pageSize: 20,
                userroletype:'',
                userdeptid:'',
                data: [],
                columns: [
                    {
                        key: 'RR',
                        title: '主机名称',
                        sortable: true
                    },
                    //{
                       // key: 'value',
                       // title: '主机IP',
                       // sortable: true
                    //},
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
                                // h('Button', {
                                //     props: {
                                //         type: 'primary',
                                //         size: 'small'
                                //     },
                                //     style: {
                                //         marginRight: '5px'
                                //     },
                                //     on: {
                                //         click: () => {
                                //             this.showmodpage(params.row);
                                //         }
                                //     }
                                // }, '修改'),
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
                                    style: {
                                        marginRight: '5px'
                                    },
                                    on: {
                                        click: () => {
                                            this.infoobj(params.row)
                                        }
                                    }
                                }, '详细')
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
                    rr: '',
                    type: '',
                    value: '',
                    ttl: '',
                    priority: ''
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
                },
                deldata:{
                    recordId:'',
                },
                modValidate:{
                    rr:[
                        {required: true,message:'主机名称不能为空！', trigger: 'blur' },
                    ],
                    type:[
                        {required: true,message:'主机类型不能为空！', trigger: 'blur' },
                    ],
                    value:[
                        {required: true,message:'主机IP不能为空！', trigger: 'blur' },
                        {validator: validateIP, trigger: 'blur' }
                    ]
                },
                addValidate:{
                    rr:[
                        {required: true,message:'主机名称不能为空！', trigger: 'blur' },
                    ],
                    type:[
                        {required: true,message:'主机类型不能为空！', trigger: 'blur' },
                    ],
                    value:[
                        {required: true,message:'主机IP不能为空！', trigger: 'blur' },
                        {validator: validateIP, trigger: 'blur' }

                    ]
                }
            };
        },
        methods: {
            query(name) {
                let _this = this;
                this.kayak.httpUtil.query({url:"aliyun/describeDomainRecords.json",method:"post",
                    params:{'pageNumber':this.pageNumber,
                            'pageSize':this.pageSize,
                            'domainName':this.querydata.domainName,
                            'rRKeyWord':this.querydata.rRKeyWord,
                            'valueKeyWord':this.querydata.valueKeyWord,
                            'roletype':this.userroletype,
                            'deptid':this.userdeptid,
                    },successAlert:false}).then(data=>{
                    _this.totalCount = data.returndata.totalCount;
                    _this.data = data.returndata.rows;
                    _this.loading = false;
                });
            },
            changePage(pageNumber) {
                this.pageNumber = pageNumber;
                this.query('querydata');
            },clean(name){
                this.$refs[name].resetFields();
            },showaddpage(name){
                this.addmodel = true;
                this.$refs[name].resetFields();
            },cleanadd(name){
                this.$refs[name].resetFields();
            },add(name){
                let _this = this;
                this.$refs[name].validate((valid) => {
                    if (valid) {
                        this.kayak.httpUtil.update({url:"aliyun/addDomainRecord.json",method:"post",params:{
                            "rr":this.adddata.rr,"type":this.adddata.type, "value":this.adddata.value, "ttl":this.adddata.ttl,"priority":this.adddata.priority
                            },successAlert:false}).then(data=>{
                            _this.addmodel = false;
                            _this.query('querydata');
                        });
                    }else {
                        this.$Message.error('请先输入正确的数据格式!');
                    }
                });
            },
            deletePage:function(row){
                let _this = this;
                this.kayak.httpUtil.query({url:"aliyun/deleteDomainRecord.json",method:"post",params:{"recordId":row.recordId},successAlert:false}).then(data=>{
                    _this.query('querydata');
                });
            },showmodpage(row){
                this.modmodel = true;
                this.moddata.recordId = row.recordId;
                this.moddata.rr = row.RR;
                this.moddata.type = row.type;
                this.moddata.value = row.value;
                this.moddata.ttl = row.TTL;
                this.moddata.priority = row.priority;
            },cleanmod(name){
                this.$refs[name].resetFields();
            },mod(name){
                let _this = this;
                this.$refs[name].validate((valid) => {
                    if (valid) {
                        this.kayak.httpUtil.update({url:"aliyun/updateDomainRecord.json",method:"post",params:{
                                'recordId':this.moddata.recordId,
                                'rr':this.moddata.rr,
                                'type':this.moddata.type,
                                'value':this.moddata.value,
                                'ttl':this.moddata.ttl,
                                priority:this.moddata.priority
                            },successAlert:false}).then(data=>{
                            _this.modmodel = false;
                            _this.query('querydata');
                        });
                    }else {
                        _this.$Message.error('请先输入正确的数据格式!');
                    }
                });
            },setstatus(row,value){
                let _this = this;
                if(value === false){
                    row.status = 'DISABLE';
                }else{
                    row.status = 'ENABLE';
                }
                this.kayak.httpUtil.update({url:"aliyun/setDomainRecordStatus.json",method:"post",params:{"recordId":row.recordId,"status":row.status},successAlert:false}).then(data=>{
                    _this.query('querydata');
                });
            },infoobj(row){
                let _this = this;
                this.kayak.httpUtil.query({url:"aliyun/describeDomainRecordInfo.json",method:"post",params:{"recordId":row.recordId},successAlert:false}).then(data=>{
                    _this.infodata.requestId = data.returndata.requestId;
                    _this.infodata.domainId= data.returndata.domainId;
                    _this.infodata.domainName= data.returndata.domainName;
                    _this.infodata.punyCode= data.returndata.punyCode;
                    _this.infodata.groupId= data.returndata.groupId;
                    _this.infodata.groupName= data.returndata.groupName;
                    _this.infodata.recordId= data.returndata.recordId;
                    _this.infodata.rr = data.returndata.rR;
                    _this.infodata.type= data.returndata.type;
                    _this.infodata.value= data.returndata.value;
                    _this.infodata.ttl= data.returndata.tTL;
                    _this.infodata.priority= data.returndata.priority;
                    _this.infodata.line= data.returndata.line;
                    _this.infodata.status= data.returndata.status;
                    _this.infodata.locked= data.returndata.locked.toString();
                    _this.infomodel = true;
                });
            },
            infoclean(){
                this.infomodel = false;
            }
        },
        mounted() {
            this.userroletype = sessionStorage.getItem("roletype");
            this.userdeptid = sessionStorage.getItem("deptid");
            this.query('querydata');

        }
    }
</script>