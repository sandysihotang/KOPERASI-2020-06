(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-5ef424a4"],{"07ac":function(t,e,a){var n=a("23e7"),i=a("6f53").values;n({target:"Object",stat:!0},{values:function(t){return i(t)}})},"6f53":function(t,e,a){var n=a("83ab"),i=a("df75"),s=a("fc6a"),o=a("d1e7").f,l=function(t){return function(e){var a,l=s(e),c=i(l),r=c.length,u=0,d=[];while(r>u)a=c[u++],n&&!o.call(l,a)||d.push(t?[a,l[a]]:l[a]);return d}};t.exports={entries:l(!0),values:l(!1)}},eccb:function(t,e,a){"use strict";a.r(e);var n=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",[a("q-table",{attrs:{dense:t.$q.screen.lt.md,title:"Daftar Pengajuan Pinjaman",data:t.data,columns:t.columns,"row-key":"id",selection:"single",selected:t.selected,filter:t.filter},on:{"update:selected":function(e){t.selected=e}},scopedSlots:t._u([{key:"top-right",fn:function(){return[a("div",{staticClass:"row"},[a("div",{staticClass:"col"},[a("q-btn",{attrs:{size:"xs",color:"green",label:"Cetak Pengajuan",icon:"print"},on:{click:t.cetakPengajuan}})],1),a("div",{staticClass:"col"},[a("q-btn",{attrs:{size:"xs",color:"green",label:"Pengajuan Baru",icon:"edit"},on:{click:t.pengajuanBaru}})],1),a("div",{staticClass:"col"},[a("q-btn",{attrs:{size:"xs",color:"green",label:"Tabel Angsuran",icon:"table"},on:{click:t.tabelAngsuran}})],1),a("div",{staticClass:"col"},[a("q-btn",{attrs:{size:"xs",color:"green",label:"Persetujuan",disable:t.getDis(),icon:"check"},on:{click:t.persetujuan}})],1),a("div",{staticClass:"col"},[a("q-input",{attrs:{borderless:"",dense:"",debounce:"300",placeholder:"Search"},scopedSlots:t._u([{key:"append",fn:function(){return[a("q-icon",{attrs:{name:"search"}})]},proxy:!0}]),model:{value:t.filter,callback:function(e){t.filter=e},expression:"filter"}})],1)])]},proxy:!0}])}),a("q-dialog",{attrs:{persistent:"","transition-show":"scale","transition-hide":"scale"},model:{value:t.persetujuanView,callback:function(e){t.persetujuanView=e},expression:"persetujuanView"}},[a("q-card",{staticStyle:{width:"700px","max-width":"80vw"}},[a("persetujuan-form",{ref:"saveFunction",attrs:{user:t.selected[0]}}),a("q-card-actions",{staticClass:"bg-white text-teal",attrs:{align:"right"}},[a("q-btn",{directives:[{name:"close-popup",rawName:"v-close-popup"}],attrs:{flat:"",label:"Close"}}),a("q-btn",{directives:[{name:"close-popup",rawName:"v-close-popup"}],attrs:{flat:"",label:"Simpan"},on:{click:t.save}})],1)],1)],1),a("q-dialog",{attrs:{persistent:"","transition-show":"scale","transition-hide":"scale"},model:{value:t.tableAngsuran,callback:function(e){t.tableAngsuran=e},expression:"tableAngsuran"}},[a("q-card",{staticStyle:{width:"700px","max-width":"80vw"}},[a("table-angsuran",{attrs:{user:t.selected[0]}}),a("q-card-actions",{staticClass:"bg-white text-teal",attrs:{align:"right"}},[a("q-btn",{directives:[{name:"close-popup",rawName:"v-close-popup"}],attrs:{flat:"",label:"Close"}})],1)],1)],1)],1)},i=[],s=(a("99af"),a("c1df")),o=a.n(s),l=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"q-pa-md"},[a("q-card",{staticClass:"full-width"},[a("q-card-section",[t._v("Idenitas Pengaju")]),a("q-card-section",[a("q-table",{attrs:{dense:t.$q.screen.lt.md,data:t.data,columns:t.columns,"row-key":"name"}})],1)],1),a("q-card",{staticClass:"full-width"},[a("q-card-section",[a("q-input",{attrs:{filled:"",label:"Total pinjaman",mask:"Rp #,###,###,###.##","fill-mask":"0","reverse-fill-mask":"","unmasked-value":"","input-class":"text-right"},model:{value:t.price,callback:function(e){t.price=e},expression:"price"}}),a("q-input",{attrs:{filled:"",label:"Tenor (Bulan)",mask:"## Bln","fill-mask":"0","reverse-fill-mask":"","unmasked-value":"","input-class":"text-right"},model:{value:t.tenor,callback:function(e){t.tenor=e},expression:"tenor"}}),a("q-select",{attrs:{filled:"",options:t.options,"stack-label":""},model:{value:t.model,callback:function(e){t.model=e},expression:"model"}})],1),a("q-card-section",[a("q-list",{attrs:{bordered:"",separator:""}},[a("q-item",[a("q-item-section",[t._v("Angsuran Pokok")]),a("q-item-section",[t._v(":")]),a("q-item-section",[t._v(t._s(t.toIDR(t.price/100/t.tenor)))])],1),a("q-item",[a("q-item-section",[t._v("Bunga(%)")]),a("q-item-section",[t._v(":")]),a("q-item-section",[t._v(t._s(t.persentase)+" %")])],1),a("q-item",[a("q-item-section",[t._v("Bunga Angsuran")]),a("q-item-section",[t._v(":")]),a("q-item-section",[t._v(t._s(t.toIDR(t.price/100*t.persentase/100)))])],1),a("q-item",[a("q-item-section",[t._v("Total Angsuran")]),a("q-item-section",[t._v(":")]),a("q-item-section",[t._v(t._s(t.toIDR(t.price/100/t.tenor+t.price/100*t.persentase/100))+" ")])],1),a("q-item",[a("q-item-section",[t._v("Total Bunga/Jasa")]),a("q-item-section",[t._v(":")]),a("q-item-section",[t._v(t._s(t.toIDR(t.price/100*t.persentase/100*t.tenor)))])],1),a("q-item",[a("q-item-section",[t._v("Total Pinjaman")]),a("q-item-section",[t._v(":")]),a("q-item-section",[t._v(t._s(t.toIDR(t.price/100+t.price/100*t.persentase/100*t.tenor))+" ")])],1)],1)],1)],1)],1)},c=[],r=(a("4ec9"),a("d3b7"),a("07ac"),a("3ca3"),a("ddb0"),{props:["user"],data:function(){return{columns:[],data:[],price:null,tenor:null,persentase:null,model:"",options:["Cancel","Approved","Rejected","On Reviewed","Awaiting Approval"]}},methods:{toIDR:function(t){for(var e="".concat(t),a="",n=0,i=e.length-1;i>=0;i--)a="".concat(a).concat(e[i]),2===n&&0!==i?(a="".concat(a,","),n=0):n++;for(var s="Rp ",o=a.length-1;o>=0;o--)s="".concat(s).concat(a[o]);return s},reload:function(){this.price=100*this.user.jumlahPinjaman,this.tenor=this.user.tenor,this.persentase=this.user.pengaturanPinjaman.bungaPinjaman},loadColumn:function(){var t=this;this.$q.loading.show(),this.$http.get("/api/getcolumnmember/".concat(this.user.id),{headers:this.$auth.getHeader()}).then((function(e){for(var a=e.data,n=0;n<a.length;n++)t.columns.push({name:a[n].cid,label:a[n].label,align:"center",field:a[n].cid,sortable:!0});t.loadData()})).catch((function(e){t.$q.loading.hide()}))},call:function(){var t=this;this.$q.loading.show();var e=new Map;e.set("Awaiting Approval",5),e.set("On Reviewed",4),e.set("Rejected",3),e.set("Approved",2),e.set("Cancel",1),this.$http.put("/api/savepinjamanfrompengurus/".concat(this.user.id),{price:this.price,tenor:this.tenor,status:e.get(this.model)},{headers:this.$auth.getHeader()}).then((function(t){})).catch((function(){t.$q.loading.hide()}))},loadData:function(){var t=this;this.$http.get("/api/getdatamember/".concat(this.user.id),{headers:this.$auth.getHeader()}).then((function(e){for(var a=e.data,n=0;n<a.length;n++){for(var i='{"id" : '.concat(a[n].id,","),s=JSON.parse(a[n].data),o=0;o<s.length;o++)if(i="".concat(i,' "').concat(s[o].uid,'":'),s[o].value instanceof Object)for(var l=Object.values(s[o].value),c=0;c<l.length;c++)i=c===l.length-1?o===s.length-1?"".concat(i," ").concat(l[c],'"'):"".concat(i," ").concat(l[c],'",'):0===c?"".concat(i,' "').concat(l[c]):"".concat(i," ").concat(l[c]);else i=o===s.length-1?"".concat(i,' "').concat(s[o].value,'"'):"".concat(i,' "').concat(s[o].value,'",');i="".concat(i," }");var r=JSON.parse(i);t.data.push(r)}t.$q.loading.hide()})).catch((function(){t.$q.loading.hide()}))}},created:function(){this.reload(),this.model=this.options[this.user.status-1],this.loadColumn()}}),u=r,d=a("2877"),h=Object(d["a"])(u,l,c,!1,null,"225f829a",null),m=h.exports,p=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"q-pa-md"},[a("q-card",{staticClass:"full-width"},[a("q-card-section",[t._v("Idenitas Pengaju")]),a("q-card-section",[a("q-table",{attrs:{dense:t.$q.screen.lt.md,data:t.data,columns:t.columns,"row-key":"name"}})],1),a("q-card-section",[a("q-table",{attrs:{dense:t.$q.screen.lt.md,data:t.dataAjuan,columns:t.columnsAjuan,"row-key":"name"}})],1)],1)],1)},f=[],g={props:["user"],data:function(){var t=this;return{columns:[],data:[],dataAjuan:[],columnsAjuan:[{name:"termin",label:"Termin Ke #",align:"center",field:function(t){return t.urutanKe},sortable:!0},{name:"status",label:"Status",align:"center",field:function(t){return t.statusBayar?"Paid":"Unpaid"},sortable:!0},{name:"angsuranPokok",label:"Angsuran Pokok",align:"center",field:function(e){return t.toIDR(e.angsuranPokok)},sortable:!0},{name:"bunga",label:"Bunga",align:"center",field:function(e){return t.toIDR(e.bunga)},sortable:!0},{name:"totalAngsuran",label:"Total Angsuran",align:"center",field:function(e){return t.toIDR(e.totalAngsuran)},sortable:!0},{name:"tglJatuhTempo",label:"Tanggal Jatuh Tempo",align:"center",field:function(t){return o.a.lang("id"),o()(t.tanggalJatuhTempo).format("dddd, Do MMMM YYYY")},sortable:!0},{name:"denda",label:"Denda",align:"center",field:function(e){return t.toIDR(null===e.denda?0:e.denda)},sortable:!0},{name:"totalTagihan",label:"Total Tagihan",align:"center",field:function(e){return t.toIDR(null===e.totalTagihan?0:e.totalTagihan)},sortable:!0},{name:"totalBayar",label:"Total Bayar",align:"center",field:function(e){return t.toIDR(null===e.totalBayar?0:e.totalBayar)},sortable:!0}]}},methods:{toIDR:function(t){for(var e="".concat(t),a="",n=0,i=e.length-1;i>=0;i--)a="".concat(a).concat(e[i]),2===n&&0!==i?(a="".concat(a,","),n=0):n++;for(var s="Rp ",o=a.length-1;o>=0;o--)s="".concat(s).concat(a[o]);return s},loadColumn:function(){var t=this;this.$q.loading.show(),this.$http.get("/api/getcolumnmember/".concat(this.user.id),{headers:this.$auth.getHeader()}).then((function(e){for(var a=e.data,n=0;n<a.length;n++)t.columns.push({name:a[n].cid,label:a[n].label,align:"center",field:a[n].cid,sortable:!0});t.loadData()})).catch((function(e){t.$q.loading.hide()}))},loadData:function(){var t=this;this.$http.get("/api/getdatamember/".concat(this.user.id),{headers:this.$auth.getHeader()}).then((function(e){for(var a=e.data,n=0;n<a.length;n++){for(var i='{"id" : '.concat(a[n].id,","),s=JSON.parse(a[n].data),o=0;o<s.length;o++)if(i="".concat(i,' "').concat(s[o].uid,'":'),s[o].value instanceof Object)for(var l=Object.values(s[o].value),c=0;c<l.length;c++)i=c===l.length-1?o===s.length-1?"".concat(i," ").concat(l[c],'"'):"".concat(i," ").concat(l[c],'",'):0===c?"".concat(i,' "').concat(l[c]):"".concat(i," ").concat(l[c]);else i=o===s.length-1?"".concat(i,' "').concat(s[o].value,'"'):"".concat(i,' "').concat(s[o].value,'",');i="".concat(i," }");var r=JSON.parse(i);t.data.push(r)}t.loaddataAjuan()})).catch((function(){t.$q.loading.hide()}))},loaddataAjuan:function(){var t=this;this.$http.get("/api/getdataajuan/".concat(this.user.id),{headers:this.$auth.getHeader()}).then((function(e){t.dataAjuan=e.data,t.$q.loading.hide()})).catch((function(){t.$q.loading.hide()}))}},created:function(){this.loadColumn()}},b=g,v=Object(d["a"])(b,p,f,!1,null,"392e8356",null),q=v.exports,j={components:{persetujuanForm:m,tableAngsuran:q},data:function(){var t=this;return{tableAngsuran:!1,status:["Cancel","Approved","Rejected","On Reviewed","Awaiting Approval","Close"],selected:[],data:[],columns:[{name:"status",label:"Status",align:"center",field:function(e){return t.status[e.status-1]},sortable:!0},{name:"noPinjaman",label:"No Pinjaman",align:"center",field:function(t){return t.kodePinjaman},sortable:!0},{name:"jumlahPinjaman",label:"Jumlah Pinjaman",align:"center",field:function(e){return t.toIDR(e.jumlahPinjaman)},sortable:!0},{name:"namaDebitur",label:"Nama Debitur",align:"center",field:function(t){return"".concat(t.user.userDetail.firstName," ").concat(t.user.userDetail.lastName)},sortable:!0},{name:"tanggalPengajuan",label:"Tanggal Pengajuan",align:"center",field:function(t){return o.a.lang("id"),o()(t.createdAt).format("dddd, Do MMMM YYYY")},sortable:!0},{name:"termin",label:"Termin (Bulan)",align:"center",field:function(t){return"".concat(t.tenor," bulan")},sortable:!0}],filter:"",persetujuanView:!1}},methods:{getDis:function(){return!(this.selected.length>0)||(2===this.selected[0].status||6===this.selected[0].status)},cetakPengajuan:function(){this.ck()||this.$swal({position:"center",type:"error",title:"Pilih item terlebih dahulu",showConfirmButton:!1,timer:1500})},save:function(){this.$refs.saveFunction.call(),this.getDataPinjaman()},pengajuanBaru:function(){this.ck()||this.$swal({position:"center",type:"error",title:"Pilih item terlebih dahulu",showConfirmButton:!1,timer:1500})},tabelAngsuran:function(){this.ck()?this.tableAngsuran=!0:this.$swal({position:"center",type:"error",title:"Pilih item terlebih dahulu",showConfirmButton:!1,timer:1500})},persetujuan:function(){this.ck()?this.persetujuanView=!0:this.$swal({position:"center",type:"error",title:"Pilih item terlebih dahulu",showConfirmButton:!1,timer:1500})},ck:function(){return this.selected.length>0},getDataPinjaman:function(){var t=this;this.$q.loading.show(),this.$http.get("/api/getdatapengajuanpinjaman",{headers:this.$auth.getHeader()}).then((function(e){t.data=e.data,t.$q.loading.hide()})).catch((function(){t.$q.loading.hide(),t.$swal({position:"center",type:"error",title:"Terjadi kesalahan, Refresh (F5)",showConfirmButton:!1,timer:1500})}))},toIDR:function(t){for(var e="".concat(t),a="",n=0,i=e.length-1;i>=0;i--)a="".concat(a).concat(e[i]),2===n&&0!==i?(a="".concat(a,","),n=0):n++;for(var s="Rp ",o=a.length-1;o>=0;o--)s="".concat(s).concat(a[o]);return s}},created:function(){this.getDataPinjaman()}},w=j,k=Object(d["a"])(w,n,i,!1,null,"50523ac9",null);e["default"]=k.exports}}]);
//# sourceMappingURL=chunk-5ef424a4.18e67fdf.js.map