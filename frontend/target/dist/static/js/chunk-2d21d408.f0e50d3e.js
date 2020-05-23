(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-2d21d408"],{d105:function(e,t,a){"use strict";a.r(t);var i=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{staticClass:"q-pa-md"},[a("div",{staticClass:"row"},[a("div",{staticClass:"col-md-2"}),a("div",{staticClass:"col-md-8"},[a("q-stepper",{ref:"stepper",attrs:{animated:"","active-color":"purple"},scopedSlots:e._u([{key:"navigation",fn:function(){return[a("q-stepper-navigation",[3===e.step?a("q-btn",{attrs:{color:"deep-orange",label:"Finish"},on:{click:e.createKoperasi}}):a("q-btn",{attrs:{color:"deep-orange",label:"Continue"},on:{click:function(t){return e.$refs.stepper.next()}}}),e.step>1?a("q-btn",{staticClass:"q-ml-sm",attrs:{flat:"",color:"deep-orange",label:"Back"},on:{click:function(t){return e.$refs.stepper.previous()}}}):e._e()],1)]},proxy:!0}]),model:{value:e.step,callback:function(t){e.step=t},expression:"step"}},[a("q-step",{attrs:{name:1,prefix:"1",title:"Basic Pertanyaan"}},[a("q-input",{attrs:{filled:"",square:"",clearable:"",type:"text",rules:[function(e){return!!e||"Field is required"}],label:"Nama Koperasi"},scopedSlots:e._u([{key:"prepend",fn:function(){return[a("q-icon",{attrs:{name:"fa fa-id-card"}})]},proxy:!0}]),model:{value:e.form.name,callback:function(t){e.$set(e.form,"name",t)},expression:"form.name"}}),a("q-input",{attrs:{filled:"",square:"",clearable:"",type:"text",rules:[function(e){return!!e||"Field is required"}],label:"Nama Pendiri Koperasi"},scopedSlots:e._u([{key:"prepend",fn:function(){return[a("q-icon",{attrs:{name:"person"}})]},proxy:!0}]),model:{value:e.form.pendiri,callback:function(t){e.$set(e.form,"pendiri",t)},expression:"form.pendiri"}}),a("q-input",{attrs:{filled:"",square:"",clearable:"",type:"text",rules:[function(e){return!!e||"Field is required"}],label:"Alamat Lengkap Koperasi"},scopedSlots:e._u([{key:"prepend",fn:function(){return[a("q-icon",{attrs:{name:"place"}})]},proxy:!0}]),model:{value:e.form.alamat,callback:function(t){e.$set(e.form,"alamat",t)},expression:"form.alamat"}}),a("q-input",{attrs:{filled:"",square:"",clearable:"",type:"text",rules:[function(e){return!!e||"Field is required"}],label:"Email Koperasi"},scopedSlots:e._u([{key:"prepend",fn:function(){return[a("q-icon",{attrs:{name:"email"}})]},proxy:!0}]),model:{value:e.form.email,callback:function(t){e.$set(e.form,"email",t)},expression:"form.email"}})],1),a("q-step",{attrs:{name:2,prefix:"2",title:"Create an ad group",caption:"Optional"}},[a("q-input",{attrs:{filled:"",square:"",rules:[function(e){return!!e||"Field is required"}],label:"Tahun Berdiri Koperasi"},scopedSlots:e._u([{key:"prepend",fn:function(){return[a("q-icon",{attrs:{name:"event"}})]},proxy:!0},{key:"append",fn:function(){return[a("q-icon",{staticClass:"cursor-pointer",attrs:{name:"access_time"}},[a("q-popup-proxy",{attrs:{"transition-show":"scale","transition-hide":"scale"}},[a("q-date",{model:{value:e.form.date,callback:function(t){e.$set(e.form,"date",t)},expression:"form.date"}})],1)],1)]},proxy:!0}]),model:{value:e.form.date,callback:function(t){e.$set(e.form,"date",t)},expression:"form.date"}}),a("q-input",{attrs:{filled:"",square:"",clearable:"",type:"text",rules:[function(e){return!!e||"Field is required"}],label:"No Izin Mendirikan Koperasi"},scopedSlots:e._u([{key:"prepend",fn:function(){return[a("q-icon",{attrs:{name:"lock"}})]},proxy:!0}]),model:{value:e.form.izin,callback:function(t){e.$set(e.form,"izin",t)},expression:"form.izin"}}),a("q-select",{ref:"input",attrs:{filled:"",square:"",clearable:"",type:"text",options:e.options,rules:["date",function(e){return!!e||"Field is required"}],label:"Jenis Koperasi"},scopedSlots:e._u([{key:"prepend",fn:function(){return[a("q-icon",{attrs:{name:"list"}})]},proxy:!0}]),model:{value:e.form.jenis,callback:function(t){e.$set(e.form,"jenis",t)},expression:"form.jenis"}})],1),a("q-step",{attrs:{name:3,prefix:"3",title:"Create an ad"}},[a("div",{staticClass:"row"},[a("div",{staticClass:"col-md-2"}),a("div",{staticClass:"col-md-8"},[a("q-uploader",{staticStyle:{width:"100%"},attrs:{factory:e.factory,label:"Icon Koperasi(Jika Ada)","auto-upload":""}})],1),a("div",{staticClass:"col-md-2"})])])],1)],1),a("div",{staticClass:"col-md-2"})])])},r=[],n=(a("b0c0"),{data:function(){return{form:{date:"",name:"",pendiri:"",alamat:"",izin:"",jenis:"",email:"",image:null},step:1,options:["Koperasi Serba Usaha (KSU)","Koperasi Simpan Pinjam (KSP)"]}},methods:{factory:function(e){this.form.image=e},createKoperasi:function(){var e=this;if(this.$q.loading.show(),this.ck()){var t=this.useFormData();this.$http.post("/api/createkoperasi",t,{headers:this.$auth.getHeader()}).then((function(){e.$q.loading.hide(),e.$http.get("/api/currentuser",{headers:e.$auth.getHeader()}).then((function(t){e.$auth.setHaveKoperasi(t.data.haveKoperasi),window.location.href="/"})).catch((function(t){e.$swal({position:"center",type:"error",title:"Ada gangguan jaringan silahkan refresh (F5)",showConfirmButton:!1,timer:1500}),e.$q.loading.hide()}))})).catch((function(){e.$q.loading.hide(),e.$swal({position:"center",type:"error",title:"Periksa kembali data yang anda isi atau refresh halaman (F5)",showConfirmButton:!1,timer:1500})}))}else this.$swal({position:"center",type:"error",title:"Lengkapi data Untuk kebutuhan Diskoperindag",showConfirmButton:!1,timer:1500})},useFormData:function(){var e=new FormData;return this.form.image&&e.append("image",this.form.image[0]),e.append("date",this.form.date),e.append("name",this.form.name),e.append("pendiri",this.form.pendiri),e.append("alamat",this.form.alamat),e.append("jenis",this.form.jenis),e.append("izin",this.form.izin),e.append("email",this.form.email),e},ck:function(){return""!==this.form.date&&""!==this.form.name&&""!==this.form.pendiri&&""!==this.form.alamat&&""!==this.form.izin&&""!==this.form.jenis&&""!==this.form.email}},created:function(){}}),o=n,s=a("2877"),l=Object(s["a"])(o,i,r,!1,null,"5fd16c14",null);t["default"]=l.exports}}]);
//# sourceMappingURL=chunk-2d21d408.f0e50d3e.js.map