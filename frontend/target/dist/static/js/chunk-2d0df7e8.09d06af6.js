(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-2d0df7e8"],{"89c4":function(t,a,e){"use strict";e.r(a);var i=function(){var t=this,a=t.$createElement,e=t._self._c||a;return e("q-layout",{attrs:{view:"hhh LpR fFf"}},[e("q-header",{staticClass:"bg-primary text-white",attrs:{elevated:""}},[e("q-toolbar",[e("q-btn",{attrs:{dense:"",flat:"",round:"",icon:"menu"},on:{click:function(a){t.left=!t.left}}}),e("q-toolbar-title",[t._v(" "+t._s(t.title)+" ")]),e("q-btn",{attrs:{flat:"",round:"",dense:"",icon:"fa fa-sign-out-alt"},on:{click:t.logOut}})],1)],1),e("q-drawer",{attrs:{"show-if-above":"",side:"left",bordered:""},model:{value:t.left,callback:function(a){t.left=a},expression:"left"}},[e("q-scroll-area",{staticClass:"fit"},[e("q-list",{attrs:{padding:""}},[e("q-item",{directives:[{name:"ripple",rawName:"v-ripple"}],attrs:{clickable:"",to:"/dashboardkoperasi"}},[e("q-item-section",{attrs:{avatar:""}},[e("q-icon",{attrs:{color:"primary",name:"fa fa-chart-bar"}})],1),e("q-item-section",[t._v("Dashboard")])],1),e("q-item",{directives:[{name:"ripple",rawName:"v-ripple"}],attrs:{clickable:"",to:"/anggotakoperasi"}},[e("q-item-section",{attrs:{avatar:""}},[e("q-icon",{attrs:{color:"primary",name:"person"}})],1),e("q-item-section",[t._v("Anggota Koperasi")])],1),e("q-expansion-item",{attrs:{"expand-separator":"",icon:"fas fa-piggy-bank",label:"Pinjaman"}},[e("q-list",{attrs:{padding:""}},[e("q-item",{directives:[{name:"ripple",rawName:"v-ripple"}],attrs:{clickable:"",to:"/pengajuanpinjaman"}},[e("q-item-section",[t._v("Pengajuan Pinjaman")])],1),e("q-item",{directives:[{name:"ripple",rawName:"v-ripple"}],attrs:{clickable:"",to:"/transaksipinjaman"}},[e("q-item-section",[t._v("Transaksi Pinjaman")])],1)],1)],1),e("q-expansion-item",{attrs:{"expand-separator":"",icon:"settings",label:"Pengaturan"}},[e("q-list",{attrs:{padding:""}},[e("q-item",{directives:[{name:"ripple",rawName:"v-ripple"}],attrs:{clickable:"",to:"/pengaturanpendaftarananggota"}},[e("q-item-section",[t._v("Form Pendaftaran Anggota")])],1),e("q-item",{directives:[{name:"ripple",rawName:"v-ripple"}],attrs:{clickable:"",to:"/pengaturanpinjaman"}},[e("q-item-section",[t._v("Pinjaman")])],1)],1)],1)],1)],1)],1),e("q-page-container",[e("router-view")],1)],1)},n=[],r=(e("99af"),{data:function(){return{left:!1,title:"Selamat datang Koperasi "}},methods:{logOut:function(){window.localStorage.clear(),window.location.href="/"},getStateKoperasi:function(){var t=this;this.$http.get("/api/getstatekoperasi",{headers:this.$auth.getHeader()}).then((function(a){t.title="".concat(t.title," ").concat(a.data)}))}},created:function(){this.getStateKoperasi()}}),s=r,o=e("2877"),l=Object(o["a"])(s,i,n,!1,null,null,null);a["default"]=l.exports}}]);
//# sourceMappingURL=chunk-2d0df7e8.09d06af6.js.map