(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-8c8faf66"],{"07ac":function(t,e,a){var n=a("23e7"),i=a("6f53").values;n({target:"Object",stat:!0},{values:function(t){return i(t)}})},"6f53":function(t,e,a){var n=a("83ab"),i=a("df75"),s=a("fc6a"),l=a("d1e7").f,r=function(t){return function(e){var a,r=s(e),o=i(r),c=o.length,u=0,d=[];while(c>u)a=o[u++],n&&!l.call(r,a)||d.push(t?[a,r[a]]:r[a]);return d}};t.exports={entries:r(!0),values:r(!1)}},ab83:function(t,e,a){"use strict";a.r(e);var n=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"q-pa-md"},[a("q-table",{attrs:{dense:t.$q.screen.lt.md,title:"Daftar Transaksi Pinjaman",data:t.data,columns:t.columns,"row-key":"id",selection:"single",selected:t.selected,filter:t.filter},on:{"update:selected":function(e){t.selected=e}},scopedSlots:t._u([{key:"top-right",fn:function(){return[a("div",{staticClass:"row"},[a("div",{staticClass:"col"},[a("q-btn",{attrs:{size:"xs",color:"green",label:"Detail Peminjaman",icon:"table"},on:{click:t.pembayaran}})],1),a("div",{staticClass:"col"},[a("q-btn",{attrs:{size:"xs",color:"green",label:"Pembayaran",icon:"check"},on:{click:t.tabelAngsuran}})],1),a("div",{staticClass:"col"},[a("q-input",{attrs:{borderless:"",dense:"",debounce:"300",placeholder:"Search"},scopedSlots:t._u([{key:"append",fn:function(){return[a("q-icon",{attrs:{name:"search"}})]},proxy:!0}]),model:{value:t.filter,callback:function(e){t.filter=e},expression:"filter"}})],1)])]},proxy:!0}])}),a("q-dialog",{attrs:{persistent:"","transition-show":"scale","transition-hide":"scale"},model:{value:t.pembayaranView,callback:function(e){t.pembayaranView=e},expression:"pembayaranView"}},[a("q-card",{staticStyle:{width:"700px","max-width":"80vw"}},[a("persetujuan-form",{attrs:{user:t.selected[0]}}),a("q-card-actions",{staticClass:"bg-white text-teal",attrs:{align:"right"}},[a("q-btn",{directives:[{name:"close-popup",rawName:"v-close-popup"}],attrs:{flat:"",label:"Close"}})],1)],1)],1),a("q-dialog",{attrs:{persistent:"","transition-show":"scale","transition-hide":"scale"},model:{value:t.tableAngsuran,callback:function(e){t.tableAngsuran=e},expression:"tableAngsuran"}},[a("q-card",{staticStyle:{width:"700px","max-width":"80vw"}},[a("table-angsuran",{attrs:{user:t.selected[0]}}),a("q-card-actions",{staticClass:"bg-white text-teal",attrs:{align:"right"}},[a("q-btn",{directives:[{name:"close-popup",rawName:"v-close-popup"}],attrs:{flat:"",label:"Close"}})],1)],1)],1)],1)},i=[],s=(a("99af"),a("e25e"),a("c1df")),l=a.n(s),r=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"q-pa-md"},[a("q-card",{staticClass:"full-width"},[a("q-card-section",[t._v("Idenitas Pengaju")]),a("q-card-section",[a("q-table",{attrs:{dense:t.$q.screen.lt.md,data:t.data,columns:t.columns,"row-key":"name"}})],1)],1),a("q-card",{staticClass:"full-width"},[a("q-card-section",[a("q-input",{attrs:{filled:"",disable:"",label:"Total pinjaman",mask:"Rp #,###,###,###.##","fill-mask":"0","reverse-fill-mask":"","unmasked-value":"","input-class":"text-right"},model:{value:t.price,callback:function(e){t.price=e},expression:"price"}}),a("q-input",{attrs:{filled:"",disable:"",label:"Tenor (Bulan)",mask:"## Bln","fill-mask":"0","reverse-fill-mask":"","unmasked-value":"","input-class":"text-right"},model:{value:t.tenor,callback:function(e){t.tenor=e},expression:"tenor"}}),a("q-select",{attrs:{filled:"",disable:"",options:t.options,"stack-label":""},model:{value:t.model,callback:function(e){t.model=e},expression:"model"}})],1),a("q-card-section",[a("q-list",{attrs:{bordered:"",separator:""}},[a("q-item",[a("q-item-section",[t._v("Angsuran Pokok")]),a("q-item-section",[t._v(":")]),a("q-item-section",[t._v(t._s(t.toIDR(parseInt(t.price/100/t.tenor))))])],1),a("q-item",[a("q-item-section",[t._v("Bunga(%)")]),a("q-item-section",[t._v(":")]),a("q-item-section",[t._v(t._s(t.persentase)+" %")])],1),a("q-item",[a("q-item-section",[t._v("Bunga Angsuran")]),a("q-item-section",[t._v(":")]),a("q-item-section",[t._v(t._s(t.toIDR(parseInt(t.price/100*t.persentase/100))))])],1),a("q-item",[a("q-item-section",[t._v("Total Angsuran")]),a("q-item-section",[t._v(":")]),a("q-item-section",[t._v(t._s(t.toIDR(parseInt(t.price/100/t.tenor+t.price/100*t.persentase/100)))+" ")])],1),a("q-item",[a("q-item-section",[t._v("Total Bunga/Jasa")]),a("q-item-section",[t._v(":")]),a("q-item-section",[t._v(t._s(t.toIDR(parseInt(t.price/100*t.persentase/100*t.tenor))))])],1),a("q-item",[a("q-item-section",[t._v("Total Pinjaman")]),a("q-item-section",[t._v(":")]),a("q-item-section",[t._v(t._s(t.toIDR(parseInt(t.price/100+t.price/100*t.persentase/100*t.tenor)))+" ")])],1)],1)],1)],1)],1)},o=[],c=(a("07ac"),{props:["user"],data:function(){return{columns:[],data:[],price:null,tenor:null,persentase:null,model:"",options:["Cancel","Approved","Rejected","On Reviewed","Awaiting Approval"]}},methods:{toIDR:function(t){for(var e="".concat(t),a="",n=0,i=e.length-1;i>=0;i--)a="".concat(a).concat(e[i]),2===n&&0!==i?(a="".concat(a,","),n=0):n++;for(var s="Rp ",l=a.length-1;l>=0;l--)s="".concat(s).concat(a[l]);return s},reload:function(){this.price=100*this.user.jumlah_pinjaman,this.tenor=this.user.tenor,this.persentase=this.user.bunga_pinjaman},loadColumn:function(){var t=this;this.$q.loading.show(),this.$http.get("/api/getcolumnmember/".concat(this.user.id),{headers:this.$auth.getHeader()}).then((function(e){for(var a=e.data,n=0;n<a.length;n++)t.columns.push({name:a[n].cid,label:a[n].label,align:"center",field:a[n].cid,sortable:!0});t.loadData()})).catch((function(e){t.$q.loading.hide()}))},loadData:function(){var t=this;this.$http.get("/api/getdatamember/".concat(this.user.id),{headers:this.$auth.getHeader()}).then((function(e){for(var a=e.data,n=0;n<a.length;n++){for(var i='{"id" : '.concat(a[n].id,","),s=JSON.parse(a[n].data),l=0;l<s.length;l++)if(i="".concat(i,' "').concat(s[l].uid,'":'),s[l].value instanceof Object)for(var r=Object.values(s[l].value),o=0;o<r.length;o++)i=o===r.length-1?l===s.length-1?"".concat(i," ").concat(r[o],'"'):"".concat(i," ").concat(r[o],'",'):0===o?"".concat(i,' "').concat(r[o]):"".concat(i," ").concat(r[o]);else i=l===s.length-1?"".concat(i,' "').concat(s[l].value,'"'):"".concat(i,' "').concat(s[l].value,'",');i="".concat(i," }");var c=JSON.parse(i);t.data.push(c)}t.$q.loading.hide()})).catch((function(){t.$q.loading.hide()}))}},created:function(){this.reload(),this.model=this.options[this.user.status-1],this.loadColumn()}}),u=c,d=a("2877"),p=Object(d["a"])(u,r,o,!1,null,"488457e0",null),m=p.exports,h=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"q-pa-md"},[a("q-table",{staticClass:"full-width",attrs:{dense:t.$q.screen.lt.md,data:t.dataAjuan,columns:t.columnsAjuan,"row-key":"id",selection:"single",selected:t.selectedAngsuran},on:{"update:selected":function(e){t.selectedAngsuran=e}}}),a("q-space"),0!==t.selectedAngsuran.length?a("q-card",[a("q-card-section",[t._v(" Detail Pembayaran ")]),a("q-card-section",[a("q-input",{attrs:{disable:"",filled:"",value:100*parseInt(t.selectedAngsuran[0].total_angsuran),label:"Total Bayar",mask:"Rp ###,###.##","fill-mask":"0","reverse-fill-mask":"","unmasked-value":"","input-class":"text-right"}}),a("br"),a("q-input",{attrs:{filled:"",mask:"date",label:"Tanggal Pembayaran",rules:["date"]},scopedSlots:t._u([{key:"append",fn:function(){return[a("q-icon",{staticClass:"cursor-pointer",attrs:{name:"event"}},[a("q-popup-proxy",{ref:"qDateProxy",attrs:{"transition-show":"scale","transition-hide":"scale"}},[a("q-date",{on:{input:function(){return t.$refs.qDateProxy.hide()}},model:{value:t.date,callback:function(e){t.date=e},expression:"date"}})],1)],1)]},proxy:!0}],null,!1,4214985005),model:{value:t.date,callback:function(e){t.date=e},expression:"date"}}),a("q-btn",{staticClass:"full-width",attrs:{color:"primary",label:"Simpan"},on:{click:t.save}})],1)],1):t._e()],1)},f=[],g={props:["user"],data:function(){var t=this;return{dataAjuan:[],selectedAngsuran:[],columnsAjuan:[{name:"termin",label:"Termin Ke #",align:"center",field:function(t){return t.urutan_ke},sortable:!0},{name:"status",label:"Status",align:"center",field:function(t){return t.status_bayar?"Paid":"Unpaid"},sortable:!0},{name:"angsuranPokok",label:"Angsuran Pokok",align:"center",field:function(e){return t.toIDR(parseInt(e.angsuran_pokok))},sortable:!0},{name:"bunga",label:"Bunga",align:"center",field:function(e){return t.toIDR(parseInt(e.bunga))},sortable:!0},{name:"totalAngsuran",label:"Total Angsuran",align:"center",field:function(e){return t.toIDR(parseInt(e.total_angsuran))},sortable:!0},{name:"tglJatuhTempo",label:"Tanggal Jatuh Tempo",align:"center",field:function(t){return l.a.lang("id"),l()(t.tanggal_jatuh_tempo).format("dddd, Do MMMM YYYY")},sortable:!0},{name:"denda",label:"Denda",align:"center",field:function(e){return t.toIDR(parseInt(null===e.denda?0:e.denda))},sortable:!0},{name:"totalTagihan",label:"Total Tagihan",align:"center",field:function(e){return t.toIDR(parseInt(null===e.total_tagihan?0:e.total_tagihan))},sortable:!0},{name:"totalBayar",label:"Total Bayar",align:"center",field:function(e){return t.toIDR(parseInt(null===e.total_bayar?0:e.total_bayar))},sortable:!0}],date:null}},methods:{toIDR:function(t){for(var e="".concat(t),a="",n=0,i=e.length-1;i>=0;i--)a="".concat(a).concat(e[i]),2===n&&0!==i?(a="".concat(a,","),n=0):n++;for(var s="Rp ",l=a.length-1;l>=0;l--)s="".concat(s).concat(a[l]);return s},loaddataAjuan:function(){var t=this;this.$q.loading.show(),this.$http.get("/api/getdatapembayaran/".concat(this.user.id),{headers:this.$auth.getHeader()}).then((function(e){t.dataAjuan=e.data,t.$q.loading.hide()})).catch((function(){t.$q.loading.hide()}))},save:function(){var t=this;null!==this.date?(this.$q.loading.show(),this.$http.put("/api/pembayaransukses/".concat(this.selectedAngsuran[0].id),{date:this.date},{headers:this.$auth.getHeader()}).then((function(e){t.loaddataAjuan(),t.$q.notify({type:"positive",message:"Pembayaran Berhasil dilakukan"}),t.$q.loading.hide()})).catch((function(){t.$q.loading.hide()}))):this.$q.notify({type:"negative",message:"Isi tanggal pembayaran"})}},created:function(){this.loaddataAjuan()}},b=g,v=Object(d["a"])(b,h,f,!1,null,"31046ebe",null),q=v.exports,_={components:{persetujuanForm:m,tableAngsuran:q},data:function(){var t=this;return{tableAngsuran:!1,status:["Cancel","Approved","Rejected","On Reviewed","Awaiting Approval","Close"],selected:[],data:[],columns:[{name:"status",label:"Status",align:"center",field:function(e){return t.status[e.status-1]},sortable:!0},{name:"noPinjaman",label:"No Pinjaman",align:"center",field:function(t){return t.kode_pinjaman},sortable:!0},{name:"jumlahPinjaman",label:"Jumlah Pinjaman",align:"center",field:function(e){return t.toIDR(parseInt(e.jumlah_pinjaman))},sortable:!0},{name:"namaDebitur",label:"Nama Debitur",align:"center",field:function(t){return"".concat(t.first_name," ").concat(t.last_name)},sortable:!0},{name:"tanggalPengajuan",label:"Tanggal Pengajuan",align:"center",field:function(t){return l.a.lang("id"),l()(t.created_at).format("dddd, Do MMMM YYYY")},sortable:!0},{name:"termin",label:"Termin (Bulan)",align:"center",field:function(t){return"".concat(t.tenor," bulan")},sortable:!0}],filter:"",pembayaranView:!1}},methods:{tabelAngsuran:function(){this.ck()?this.tableAngsuran=!0:this.$swal({position:"center",type:"error",title:"Pilih item terlebih dahulu",showConfirmButton:!1,timer:1500})},pembayaran:function(){this.ck()?this.pembayaranView=!0:this.$swal({position:"center",type:"error",title:"Pilih item terlebih dahulu",showConfirmButton:!1,timer:1500})},ck:function(){return this.selected.length>0},getDataPinjaman:function(){var t=this;this.$q.loading.show(),this.$http.get("/api/getdatapengajuanapprove",{headers:this.$auth.getHeader()}).then((function(e){t.data=e.data,t.$q.loading.hide()})).catch((function(){t.$q.loading.hide(),t.$swal({position:"center",type:"error",title:"Terjadi kesalahan, Refresh (F5)",showConfirmButton:!1,timer:1500})}))},toIDR:function(t){for(var e="".concat(t),a="",n=0,i=e.length-1;i>=0;i--)a="".concat(a).concat(e[i]),2===n&&0!==i?(a="".concat(a,","),n=0):n++;for(var s="Rp ",l=a.length-1;l>=0;l--)s="".concat(s).concat(a[l]);return s}},created:function(){this.getDataPinjaman()}},k=_,w=Object(d["a"])(k,n,i,!1,null,"356c7cbf",null);e["default"]=w.exports}}]);
//# sourceMappingURL=chunk-8c8faf66.63831075.js.map