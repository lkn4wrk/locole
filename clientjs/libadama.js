function AdamaTree(){function i(e){if(Array.isArray(e))for(var t=[],n=0;n<e.length;n++)t.push(i(e[n]));else{if("object"!=typeof e)return e;t={};for(key in e)"#"!=key[0]&&"__key"!=key&&"@o"!=key&&(t[key]=i(e[key]))}return t}function T(e,t){if(0==e.length)return e;for(var n=[],i=0;i<e.length;i++)t in e[i]&&n.push(e[i][t]);return n}function N(e,t,n){for(var i=0;i<e.length;i++){var r=e[i];if("@e"in r)for(var o=r["@e"],a=0;a<o.length;a++)n.push(function(){this.f(this.v)}.bind({f:o[a],v:t}))}}function O(e,t,n,i){for(var r in t){var o="#"+r,a=e[r],c=t[r];if(null==c)Array.isArray(a)&&(o in e&&P(T(n,o)),delete e[o]),P(T(n,r)),delete e[r];else if("object"!=typeof c||Array.isArray(c))e[r]=c,N(T(n,r),c,i);else if(Array.isArray(a)||"@o"in c||"@s"in c){r in e||(e[r]=[]),o in e||(e[o]={},"@o"in c&&(e[o]["@o"]=!0),e[o].__key=r);var s,u=e[o],d=T(n,r),l=null,f=null,h={};for(s in c){var p=c[s];if("@o"==s)l=p;else if("@s"==s)f=p;else if(null==p)s in u&&(h[s]=function(e){P(T(d,e)),delete u[e],R(d,e);for(var t=d,n=e,i=0;i<t.length;i++){var r=t[i];if("-"in r){r=r["-"];if("@e"in r)for(var o=r["@e"],a=0;a<o.length;a++)o[a](n)}}});else{if(!(s in u)){b=w=g=y=m=void 0;for(var _=d,v=s,m=0;m<_.length;m++){var y=_[m];if("+"in y){var g=y["+"];if("@e"in g)for(var w=g["@e"],b=0;b<w.length;b++)L(y,w[b](v),v)}}}p=c[s];"object"==typeof p?(s in u||(u[s]={},u[s].__key=s),h[s]=function(e){O(u[e],c[e],T(d,e),i),N(T(d,e),u[e],i)}):(u[s]=p,h[s]=function(e){N(T(d,e),u[e],i)})}}var k=e[r],x=[];if(null!=l){for(var S=[],C=[],A=0;A<l.length;A++){var q=l[A],D=typeof q;if("string"==D||"number"==D)S.push(u[q]),C.push(""+q);else for(var D=q[0],I=q[1],E=D;E<=I;E++)S.push(k[E]),C.push(k[E].__key)}x.push(function(){N(T(d,"~"),C,i)}),e[r]=S}else if(null!=f){for(S=[],C=[],A=0;A<f;A++)S[A]=k[A],C.push(""+A);x.push(function(){N(T(d,"~"),C,i)}),e[r]=S}for(s in h)h[s](s);for(A=0;A<x.length;A++)x[A]();N(d,e[r],i)}else{r in e&&"object"==typeof e[r]||(e[r]={});a=T(n,r);O(e[r],c,a,i),N(a,e[r],i)}}N(n,e,i)}function r(e){for(var t=0;t<e.length;t++)e[t]()}function o(e,t){if(Array.isArray(e))for(var n=0;n<e.length;n++)o(e[n],t);else if("object"==typeof e)for(var i in e)"@e"==i?o(e[i],t):(i in t||(t[i]={}),o(e[i],t[i]));else"function"==typeof e&&("@e"in t?t["@e"].push(e):t["@e"]=[e])}function u(e){var t,n={};for(t in e)if("#"!=t[0]&&"@o"!=t&&"__key"!=t){var i=e[t];if("#"+t in e){var r=e["#"+t],o={};if("@o"in r){for(var a=[],c=0;c<i.length;c++){var s=i[c];o[s.__key]=u(s),a.push(i[c].__key)}o["@o"]=a}else{for(c=0;c<i.length;c++)o[""+c]=u(r[c]);o["@s"]=i.length}n[t]=o}else n[t]="object"==typeof i?u(i):i}return n}var a=0,c={},s={},L=(this.nuke=function(){s={}},this.copy=function(){return i(c)},this.str=function(){return JSON.stringify(c)},function(e,t,n){if(Array.isArray(t))for(var i=0;i<t.length;i++)L(e,t[i],n);else if("function"==typeof t)n in e||(e[n]={}),"@e"in(o=e[n])?o["@e"].push(t):o["@e"]=[t];else if("object"==typeof t){n in e||(e[n]={});var r,o=e[n];for(r in t)"@e"==r?L(e,t[r],n):L(o,t[r],r)}}),P=function(e){if(Array.isArray(e))for(var t=e.length,n=0;n<t;n++)P(e[n]);else if("object"==typeof e)for(var i in e){var r=e[i];if("@e"==i)for(var o=0;o<r.length;o++)r[o](null);else P(r)}},R=function(e,t){if(Array.isArray(e))for(var n=e.length,i=0;i<n;i++)R(e[i],t);else"object"==typeof e&&t in e&&delete e[t]};this.update=function(e){var t,n=[];for(t in s)n.push(s[t]);var i=[];O(c,e,n,i),r(i)};this.subscribe=function(e){var t={},e=(o(e,t),u(c)),n=[],i=(O({},e,[t],n),r(n),""+a++);return s[i]=t,function(){delete s[i]}}}class WebSocketAdamaConnection{constructor(e){this.backoff=1,this.host=e,this.url="wss://"+e+"/~s",this.connected=!1,this.assets=!0,this.dead=!1,this.maximum_backoff=5e3,this.reset_backoff=1e3,this.socket=null,this.onstatuschange=function(e){},this.onping=function(e,t){},this.scheduled=!1,this.callbacks=new Map,this.nextId=0,this.onreconnect=new Map}stop(){this.dead=!0,null!==this.socket&&this.socket.close()}_retry(){var e,t;this.socket=null,this.connected&&(this.connected=!1,this.onstatuschange(!1)),this.callbacks.clear(),this.dead||this.scheduled||(e=!1,this.backoff+=Math.random()*this.backoff,this.backoff>this.maximum_backoff&&(this.backoff=this.maximum_backoff,e=!0),this.scheduled=!0,t=this,setTimeout(function(){t.start()},this.backoff),e&&(this.backoff/=2))}start(){var n=this;this.scheduled=!1,this.dead=!1,this.socket=new WebSocket(this.url),this.socket.onmessage=function(e){var t,e=JSON.parse(e.data);{if(!("ping"in e))return"status"in e?"connected"!=e.status?(n.socket.close(),n.socket=null,n.backoff=n.reset_backoff,void n._retry()):(n.backoff=1,n.connected=!0,n.assets=e.assets,n.onstatuschange(!0),n._reconnect(),void n.ConfigureMakeOrGetAssetKey({success:function(e){try{var t=new XMLHttpRequest;t.open("GET","https://"+n.host+"/~p"+e.assetKey,!0),t.withCredentials=!0,t.send()}catch(e){console.log(e)}},failure:function(){}})):void("failure"in e?n.callbacks.has(e.failure)&&(t=n.callbacks.get(e.failure))&&(n.callbacks.delete(e.failure),t(e)):"deliver"in e&&n.callbacks.has(e.deliver)&&(t=n.callbacks.get(e.deliver))&&(e.done&&n.callbacks.delete(e.deliver),t(e)));n.onping(e.ping,e.latency),e.pong=(new Date).getTime()/1e3,n.socket.send(JSON.stringify(e))}},this.socket.onclose=function(e){n._retry()},this.socket.onerror=function(e){n._retry()}}_write(e,t){this.connected?(this.callbacks.set(e.id,t),this.socket.send(JSON.stringify(e))):t({failure:600,reason:9999})}async wait_connected(){var n,i;return this.connected?new Promise(function(e){e(!0)}):(i=(n=this).onstatuschange,new Promise(function(t){n.onstatuschange=function(e){i(e),e&&(t(!0),n.onstatuschange=i)}}))}_reconnect(){this.onreconnect.forEach(function(e,t){e.__retry()})}__execute_rr(t){var n=this;return t.first=!0,n._write(t.request,function(e){t.first&&(t.first=!1,"failure"in e?"failure"in t.responder&&t.responder.failure(e.reason):"success"in t.responder&&t.responder.success(e.response)),n.onreconnect.delete(t.id)}),n.onreconnect.set(t.id,t),t.__retry=function(){n.__execute_rr(t)},t}__execute_stream(t){var n=this;return n._write(t.request,function(e){"failure"in e?("failure"in t.responder&&t.responder.failure(e.reason),n.onreconnect.delete(t.id)):(e.response&&"next"in t.responder&&t.responder.next(e.response),e.done&&("complete"in t.responder&&t.responder.complete(),n.onreconnect.delete(t.id)))}),n.onreconnect.set(t.id,t),t.__retry=function(){n.__execute_stream(t)},t}__id(){return this.nextId++,this.nextId}InitSetupAccount(e,t){var n=this.__id();return this.__execute_rr({id:n,responder:t,request:{method:"init/setup-account",id:n,email:e}})}InitConvertGoogleUser(e,t){var n=this.__id();return this.__execute_rr({id:n,responder:t,request:{method:"init/convert-google-user",id:n,"access-token":e}})}InitCompleteAccount(e,t,n,i){var r=this.__id();return this.__execute_rr({id:r,responder:i,request:{method:"init/complete-account",id:r,email:e,revoke:t,code:n}})}AccountSetPassword(e,t,n){var i=this.__id();return this.__execute_rr({id:i,responder:n,request:{method:"account/set-password",id:i,identity:e,password:t}})}AccountGetPaymentPlan(e,t){var n=this.__id();return this.__execute_rr({id:n,responder:t,request:{method:"account/get-payment-plan",id:n,identity:e}})}AccountLogin(e,t,n){var i=this.__id();return this.__execute_rr({id:i,responder:n,request:{method:"account/login",id:i,email:e,password:t}})}Probe(e,t){var n=this.__id();return this.__execute_rr({id:n,responder:t,request:{method:"probe",id:n,identity:e}})}AuthorityCreate(e,t){var n=this.__id();return this.__execute_rr({id:n,responder:t,request:{method:"authority/create",id:n,identity:e}})}AuthoritySet(e,t,n,i){var r=this.__id();return this.__execute_rr({id:r,responder:i,request:{method:"authority/set",id:r,identity:e,authority:t,"key-store":n}})}AuthorityGet(e,t,n){var i=this.__id();return this.__execute_rr({id:i,responder:n,request:{method:"authority/get",id:i,identity:e,authority:t}})}AuthorityList(e,t){var n=this.__id();return this.__execute_stream({id:n,responder:t,request:{method:"authority/list",id:n,identity:e}})}AuthorityDestroy(e,t,n){var i=this.__id();return this.__execute_rr({id:i,responder:n,request:{method:"authority/destroy",id:i,identity:e,authority:t}})}SpaceCreate(e,t,n,i){var r=this.__id();return this.__execute_rr({id:r,responder:i,request:{method:"space/create",id:r,identity:e,space:t,template:n}})}SpaceGenerateKey(e,t,n){var i=this.__id();return this.__execute_rr({id:i,responder:n,request:{method:"space/generate-key",id:i,identity:e,space:t}})}SpaceUsage(e,t,n,i){var r=this.__id();return this.__execute_stream({id:r,responder:i,request:{method:"space/usage",id:r,identity:e,space:t,limit:n}})}SpaceGet(e,t,n){var i=this.__id();return this.__execute_rr({id:i,responder:n,request:{method:"space/get",id:i,identity:e,space:t}})}SpaceSet(e,t,n,i){var r=this.__id();return this.__execute_rr({id:r,responder:i,request:{method:"space/set",id:r,identity:e,space:t,plan:n}})}SpaceRedeployKick(e,t,n){var i=this.__id();return this.__execute_rr({id:i,responder:n,request:{method:"space/redeploy-kick",id:i,identity:e,space:t}})}SpaceSetRxhtml(e,t,n,i){var r=this.__id();return this.__execute_rr({id:r,responder:i,request:{method:"space/set-rxhtml",id:r,identity:e,space:t,rxhtml:n}})}SpaceGetRxhtml(e,t,n){var i=this.__id();return this.__execute_rr({id:i,responder:n,request:{method:"space/get-rxhtml",id:i,identity:e,space:t}})}SpaceDelete(e,t,n){var i=this.__id();return this.__execute_rr({id:i,responder:n,request:{method:"space/delete",id:i,identity:e,space:t}})}SpaceSetRole(e,t,n,i,r){var o=this.__id();return this.__execute_rr({id:o,responder:r,request:{method:"space/set-role",id:o,identity:e,space:t,email:n,role:i}})}SpaceReflect(e,t,n,i){var r=this.__id();return this.__execute_rr({id:r,responder:i,request:{method:"space/reflect",id:r,identity:e,space:t,key:n}})}SpaceList(e,t,n,i){var r=this.__id();return this.__execute_stream({id:r,responder:i,request:{method:"space/list",id:r,identity:e,marker:t,limit:n}})}DomainMap(e,t,n,i,r){var o=this.__id();return this.__execute_rr({id:o,responder:r,request:{method:"domain/map",id:o,identity:e,domain:t,space:n,certificate:i}})}DomainMapDocument(e,t,n,i,r,o){var a=this.__id();return this.__execute_rr({id:a,responder:o,request:{method:"domain/map-document",id:a,identity:e,domain:t,space:n,key:i,certificate:r}})}DomainList(e,t){var n=this.__id();return this.__execute_stream({id:n,responder:t,request:{method:"domain/list",id:n,identity:e}})}DomainUnmap(e,t,n){var i=this.__id();return this.__execute_rr({id:i,responder:n,request:{method:"domain/unmap",id:i,identity:e,domain:t}})}DomainGet(e,t,n){var i=this.__id();return this.__execute_rr({id:i,responder:n,request:{method:"domain/get",id:i,identity:e,domain:t}})}DocumentCreate(e,t,n,i,r,o){var a=this.__id();return this.__execute_rr({id:a,responder:o,request:{method:"document/create",id:a,identity:e,space:t,key:n,entropy:i,arg:r}})}DocumentDelete(e,t,n,i){var r=this.__id();return this.__execute_rr({id:r,responder:i,request:{method:"document/delete",id:r,identity:e,space:t,key:n}})}DocumentList(e,t,n,i,r){var o=this.__id();return this.__execute_stream({id:o,responder:r,request:{method:"document/list",id:o,identity:e,space:t,marker:n,limit:i}})}MessageDirectSend(e,t,n,i,r,o,a){var c=this.__id();return this.__execute_rr({id:c,responder:a,request:{method:"message/direct-send",id:c,identity:e,space:t,key:n,"viewer-state":i,channel:r,message:o}})}MessageDirectSendOnce(e,t,n,i,r,o,a){var c=this.__id();return this.__execute_rr({id:c,responder:a,request:{method:"message/direct-send-once",id:c,identity:e,space:t,key:n,dedupe:i,channel:r,message:o}})}ConnectionCreate(e,t,n,i,r){var s=this,u=s.__id();return s.__execute_stream({id:u,responder:r,request:{method:"connection/create",id:u,identity:e,space:t,key:n,"viewer-state":i},send:function(e,t,n){var i=s.__id();s.__execute_rr({id:i,responder:n,request:{method:"connection/send",id:i,connection:u,channel:e,message:t}})},sendOnce:function(e,t,n,i){var r=s.__id();s.__execute_rr({id:r,responder:i,request:{method:"connection/send-once",id:r,connection:u,channel:e,dedupe:t,message:n}})},canAttach:function(e){var t=s.__id();s.__execute_rr({id:t,responder:e,request:{method:"connection/can-attach",id:t,connection:u}})},attach:function(e,t,n,i,r,o,a){var c=s.__id();s.__execute_rr({id:c,responder:a,request:{method:"connection/attach",id:c,connection:u,"asset-id":e,filename:t,"content-type":n,size:i,"digest-md5":r,"digest-sha384":o}})},update:function(e,t){var n=s.__id();s.__execute_rr({id:n,responder:t,request:{method:"connection/update",id:n,connection:u,"viewer-state":e}})},end:function(e){var t=s.__id();s.__execute_rr({id:t,responder:e,request:{method:"connection/end",id:t,connection:u}})}})}DocumentsHashPassword(e,t){var n=this.__id();return this.__execute_rr({id:n,responder:t,request:{method:"documents/hash-password",id:n,password:e}})}ConfigureMakeOrGetAssetKey(e){var t=this.__id();return this.__execute_rr({id:t,responder:e,request:{method:"configure/make-or-get-asset-key",id:t}})}AttachmentStart(e,t,n,i,r,o){var a=this,c=a.__id();return a.__execute_stream({id:c,responder:o,request:{method:"attachment/start",id:c,identity:e,space:t,key:n,filename:i,"content-type":r},append:function(e,t,n){var i=a.__id();a.__execute_rr({id:i,responder:n,request:{method:"attachment/append",id:i,upload:c,"chunk-md5":e,"base64-bytes":t}})},finish:function(e){var t=a.__id();a.__execute_rr({id:t,responder:e,request:{method:"attachment/finish",id:t,upload:c}})}})}SuperCheckIn(e,t){var n=this.__id();return this.__execute_rr({id:n,responder:t,request:{method:"super/check-in",id:n,identity:e}})}SuperListAutomaticDomains(e,t,n){var i=this.__id();return this.__execute_stream({id:i,responder:n,request:{method:"super/list-automatic-domains",id:i,identity:e,timestamp:t}})}SuperSetDomainCertificate(e,t,n,i,r){var o=this.__id();return this.__execute_rr({id:o,responder:r,request:{method:"super/set-domain-certificate",id:o,identity:e,domain:t,certificate:n,timestamp:i}})}}var Adama={Production:"aws-us-east-2.adama-platform.com",Connection:WebSocketAdamaConnection},RxHTML=function(){function o(e){return e.startsWith("/")?G+e.substring(1):e}function s(e){var d;return e in q?q[e]:((d={name:e,ptr:null,tree:new AdamaTree,outstanding:{},decisions:{},choice_subs:{},resets:{},connection_events:{},id:0,connection_state:!1,choices:{}}).set_connected=function(e){if(this.connection_state!=e){this.connection_state=e;var t,n=[];for(t in d.connection_events)d.connection_events[t](e)||n.push(t);for(var i=0;i<n.length;i++)delete d.connection_events[n[i]]}}.bind(d),d.connected=function(e){var t="-|"+this.id++;return(this.connection_events[t]=e)(this.connection_state),function(){delete this.connection_events[t]}.bind(this)}.bind(d),d.subscribe_any=function(e){var t="-|"+this.id++;return this.decisions[t]=e,function(){delete this.decisions[t]}.bind(this)}.bind(d),d.subscribe=function(e,t){var n=e+"|"+this.id++;return this.decisions[n]=t,function(){delete this.decisions[n]}.bind(this)}.bind(d),d.subscribe_reset=function(e){var t="reset|"+this.id++;return this.resets[t]=e,function(){delete this.resets[t]}.bind(this)}.bind(d),d.subscribe_choice=function(e,t){var n=e+"|"+this.id++;return this.choice_subs[n]=t,function(){delete this.choice_subs[n]}.bind(this)}.bind(d),d.onchoices=function(e,t){var n,i=[];for(n in d.choice_subs)!n.startsWith(e+"|")||d.choice_subs[n](t)||i.push(n);for(var r=0;r<i.length;r++)delete d.choice_subs[i[r]]},d.ondecide=function(e){var t,n=[];for(t in d.resets)(0,d.resets[t])()||n.push(t);for(var i=0;i<n.length;i++)delete d.resets[n[i]];for(r in d.outstanding)d.outstanding[r]={options:[]};for(var r,o=e.length,i=0;i<o;i++){var a=e[i];d.outstanding[a.channel]=a}for(r in d.outstanding){var c,s=d.outstanding[r],u=[];for(c in d.decisions)!c.startsWith(r+"|")&&!c.startsWith("-|")||d.decisions[c](s,r)||u.push(c);for(i=0;i<u.length;i++)delete d.decisions[u[i]]}},q[e]=d)}function f(e,t,n){e=S.pI(e,t),"@e"in(t=e[e.current]).delta?t.delta["@e"].push(n):t.delta["@e"]=[n]}function a(e){return{tree:new AdamaTree,delta:{},parent:null,path:null,where:e}}function d(e){if(null==e)return null;var t=null,n={};return null!=(t=null!=e.parent?d(e.parent):t)&&(t.delta[e.path]=n),{tree:e.tree,parent:t,delta:n,path:e.path}}function c(e,t){var n;return null!=e.parent?((n={})[e.path]=t,c(e.parent,n)):t}function l(e){for(var t=e;null!=t.parent;)t=t.parent;return t}function h(e){for(var t=e.lastChild;t;)e.removeChild(t),t=e.lastChild}function u(e,t){var n={inflight:!1,timeout:null,inflight:!1};return function(){n.inflight||(n.inflight=!0,n.timeout=window.setTimeout(function(){n.inflight=!1,n.timeout=null,t()},e))}}function p(){return{__data:function(){},__view:function(){}}}function _(e){e.__data=function(){},e.__view=function(){}}function v(e){e.__data(),e.__view()}function m(e,t){null!=e.data?t.__data=e.data.tree.subscribe(l(e.data).delta):t.__data=function(){},null!=e.view?t.__view=e.view.tree.subscribe(l(e.view).delta):t.__view=function(){}}function y(e){var t={service:e.service,data:d(e.data),view:d(e.view),current:e.current};return null!=t.data&&(t.data.connection=e.data.connection),t}function g(e,t,n,i){if(t in e.data.connection.outstanding)for(var r=e.data.connection.outstanding[t].options,o=0;o<r.length;o++){var a=r[o];if(n in a&&a[n]==i)return a}return null}function w(e){e.dispatchEvent(new Event("success"))}function b(e){e.dispatchEvent(new Event("failed"))}function k(e,t){var n,e=e.data.connection.choices,i=(t in e||(e[t]={}),e[t]),r=[];for(n in i)r.push(i[n]);return r}function x(e){if("INPUT"==e.tagName.toUpperCase())"email"==e.type&&"email"==e.name&&(e.value=localStorage.getItem("email_remember"));else if("children"in e)for(var t=e.children.length,n=0;n<t;n++)x(e.children[n])}var e,t,S={},r={},C={},A=new Adama.Connection(Adama.Production),q={},D=document.createElement("div"),n=document.createElement("span"),U=document.createElement("span"),G=(A.onstatuschange=function(e){U.innerHTML=e?"Yes":"No",e?A.onping=function(e,t){1<=t&&(n.innerHTML=""+t)}:(n.innerHTML="",A.onping=function(e,t){})},D.appendChild(U),D.appendChild(n),D.style="position:fixed; bottom:0px; right:0px",A.start(),"/"),i=function(e){return e},I=(window.location.hostname.endsWith(".adama-platform.com")&&!window.location.hostname.endsWith("ide.adama-platform.com")&&(e=window.location.pathname.split("/"),G=[e[0],e[1],e[2],""].join("/"),t=e[0].length+e[1].length+e[2].length+2,i=function(e){return e.substring(t)}),S.getConnectionByName=s,S.make=function(){return new AdamaTree},S.subscribe=f,S.fresh=a,S.makeDeltaCopy=d,S.pathTo=c,S.rootOf=l,S.pV=function(e){return{service:e.service,data:e.data,view:e.view,current:"view"}},S.newStateViewOf=S.pV,S.pD=function(e){return{service:e.service,data:e.data,view:e.view,current:"data"}},S.newStateDataOf=S.pD,S.pR=function(e){for(var t={service:e.service,data:e.data,view:e.view,current:e.current},n=t[e.current];null!=n.parent;){if(null==n.parent)return t[e.current],n;n=n.parent}return t[e.current]=n,t},S.newStateRootOf=S.pR,S.pU=function(e){var t={service:e.service,data:e.data,view:e.view,current:e.current},n=t[e.current];return null!=n.parent&&(t[e.current]=n),t},S.newStateParentOf=S.pU,S.pI=function(e,t){var n=e[e.current],i=(t in n.delta||(n.delta[t]={}),{service:e.service,data:e.data,view:e.view,current:e.current});return i[e.current]={tree:n.tree,delta:n.delta[t],parent:n,path:t},"data"==i.current&&(i.data.connection=n.connection),i},S.newStateDiveInto=S.pI,S.pEV=function(e,t){return t in e.view.delta||(e.view.delta[t]={}),{service:e.service,data:e.data,view:{tree:e.view.tree,delta:e.view.delta[t],parent:e.view,path:t},current:e.current}},S.newStateCreateViewChild=S.pEV,S.pIE=function(e,t,n){e=S.pI(e,t);return n?S.pEV(e,t):e},S.Y=function(e,t,n,i){f(e,n,function(e){t[n]=e,i()})},S.Y2=function(e,n,i,r,o){f(e,r,function(e){var t=n._[i];return t[r]=e,o(t),!0})},S.RX=function(e){for(var t={_:{}},n=0;n<e.length;n++)t._[e[n]]={};return t},S.YS=function(e,t,n){f(e,n,function(e){return t[n]=e,!0})},S.T=function(e){return document.createTextNode(e)},S.L=function(e,t){var n=document.createTextNode("");return f(e,t,function(e){n.nodeValue=e}),n},S.LT=function(e,t,n){var i=document.createTextNode("");return f(e,t,function(e){null!=e&&(i.nodeValue=n(e))}),i},S.E=function(e,t){return null==t?document.createElement(e):((e=document.createElementNS(t,e)).setAttribute("xmlns",t),e)},S.P=function(i,r,e,o,a){var c=p();e.__=function(){var n;"name"in e&&this.name!=e.name&&(n=s(e.name),this.name=e.name,n.connected(function(e){h(i),v(c);var t={service:r.service,data:{connection:n,tree:n.tree,delta:{},parent:null,path:null},view:d(r.view),current:"data"};return(e?o:a)(i,t),m(t,c),!0}))}.bind({name:""})},S.TP=function(e,t){r[e]=t},S.HREF=function(e,n){e.setAttribute("href",o(n)),e.onclick=function(e){var t=(n.startsWith("/")?n.substring(1):n).split("/");return!L(t,0,C,{})||(e.preventDefault(),S.run(document.body,n,!0),!1)}},S.ACLASS=function(e,t){e.setAttribute("class",t)},S.ASRC=function(e,t){e.setAttribute("src",t)},S.UT=function(e,t,n,i){(0,r[n])(e,t,i)},S.SW=function(n,i,e,r){var t={prior:null},t=(_(t),function(e){var t;e!=this.prior&&(this.prior=e,v(this),h(n),t=y(i),r(n,t,""+e),m(t,this))}.bind(t));f(i,e,t)},S.IT=function(r,e,t,o,a){var c=S.pIE(e,t,o),s={},u={};f(e,t,{"+":function(e){var t={service:(t=S.pIE(c,e,o)).service,data:t.data,view:d(t.view),current:t.current},n=p(),i=a(t);return s[e]=i,u[e]=n,r.append(i),e=n,null!=(i=t).view?e.__view=i.view.tree.subscribe(l(i.view).delta):e.__view=function(){},t[t.current].delta},"-":function(e){e in s&&(r.removeChild(s[e]),delete s[e]),e in u&&(v(u[e]),delete u[e])},"~":function(e){h(r);for(var t=0;t<e.length;t++)r.append(s[e[t]])}})},{}),M=(S.PRCUAC=function(e,t){I[e]=t},S.exCC=function(e,t,n,i){e.addEventListener(t,function(){i in I&&I[i]()})},S.aCC=function(t,n,i,e){var r=T(n,e);t.onsubmit=function(e){i in I?(e.preventDefault(),e=E(t),I[i](e,n,r,S),w(t)):b(t)}},S.exFIN=function(e,t,n,i){e.addEventListener(t,function(){function t(){delete n.data.connection.choices[i],n.data.connection.onchoices(i,{})}var e=k(n,i);n.data.connection.ptr.send(i,e,{failure:function(e){t(),console.log("failed:"+e)},success:function(e){t(),console.log("Success|"+e.seq)}})})},S.FIN=function(n,i,r,o,e,a,c){var s={owner:n,shown:!1};_(s);s.update=function(){var e,t=i.data.connection.outstanding[r];t&&(e=k(i,r),e=!("min"in t&&"max"in t)||t.min<=e.length&&e.length<=t.max,s.eval!=e&&(s.eval=e,t=s.eval,v(s),h(n),e=y(i),(t===o?a:c)(n,e),m(e,s)))},i.data.connection.subscribe_choice(r,function(){return s.update(),!0})},S.exCH=function(e,t,i,n,r,o){var a={value:null};e.addEventListener(t,function(){var e,t,n=g(i,r,o,a.value);null!=n&&(e=i.data.connection.choices,r in e||(e[r]={}),e=e[r],(t=n[o])in e?delete e[t]:e[t]=n,i.data.connection.onchoices(r,e))}),f(i,n,function(e){a.value=e})},S.exD=function(e,t,n,i,r,o){var a={value:null};e.addEventListener(t,function(){var e=g(n,r,o,a.value);if(null!=e){let t=performance.now();n.data.connection.ptr.send(r,e,{failure:function(e){},success:function(e){console.log("Success|"+e.seq+";latency="+(performance.now()-t))}})}}),f(n,i,function(e){a.value=e})},S.onFORCE_AUTH=function(e,t,n,i){function r(){P[n]=i,localStorage.setItem("identity_"+n,i)}"load"==t?window.setTimeout(r,1):e.addEventListener(t,r)},S.onS=function(e,t,n,i,r){function o(){(e={})[i]="function"==typeof r?r():r;var e=c(n[n.current],e);n[n.current].tree.update(e)}"load"==t?window.setTimeout(o,1):e.addEventListener(t,o)},S.onT=function(e,t,n,i){var r={value:!1};e.addEventListener(t,function(){var e={},e=(e[i]=!r.value,c(n[n.current],e));n[n.current].tree.update(e)}),f(n,i,function(e){r.value=1==e})},S.onD=function(e,t,n,i,r){var o={value:0};e.addEventListener(t,function(){var e={},e=(e[i]=o.value+r,c(n[n.current],e));n[n.current].tree.update(e)}),f(n,i,function(e){"number"==typeof e?o.value=e:(e=parseFloat(e),isNaN(e)||(o.value=e))})},S.CSEN=function(n,i,e,r,t,o,a,c,s,u){var d={value:"",owner:n,shown:!1,eval:null};_(d);d.update=function(){var e,t;i.data.connection.outstanding[r]&&(e=i.data.connection.choices,r in e||(e[r]={}),e=e[r],e=d.value in e,d.eval!=e&&(d.eval=e,e=d.eval,v(d),h(n),t=y(i),(e===a?s:u)(n,t),m(t,d)))},i.data.connection.subscribe_choice(r,function(){return d.update(),!0}),f(e,o,function(e){d.value=e,d.update()})},S.DE=function(n,i,e,r,o,t,a,c,s,u){var d={value:"",owner:n,shown:!1,eval:null};_(d);d.update=function(){var e,t=null!=g(i,r,o,d.value);d.eval!=t&&(d.eval=t,t=d.eval,v(d),h(n),e=y(i),(t===a?s:u)(n,e),m(e,d))},i.data.connection.subscribe(r,function(){return d.update(),!0}),f(e,t,function(e){d.value=e,d.update()})},S.IF=function(r,o,a,c,s,u,d){var l=p(),e=function(e){var t,n,i=!!e===c;this.shown!=i&&(this.shown=i,h(r),v(l),n=t=y(o),"object"==typeof e&&(n=S.pI(n,a),s&&(n=S.pEV(n,a))),(i?u:d)(r,n),m(t,l))}.bind({shown:"no"});f(o,a,e)},S.aCP=function(n,i,r){n.onsubmit=function(e){e.preventDefault();var e=E(n),t=("."!=r&&""!=r&&((t={})[r]=e,e=t),c(i.view,e));i.view.tree.update(t),w(n)}},S.SY=function(n,i,r,e){function t(e){(t={})[r]=n.value;var t=c(i.view,t);i.view.tree.update(t)}var o="type"in n?n.type.toUpperCase():"text";"CHECKBOX"==o?n.onchange=u(e,function(e){t(n.checked)}):"RADIO"==o?n.onchange=u(e,function(e){n.checked&&t(n.value)}):(n.onchange=u(e,function(e){t(n.value)}),n.onkeyup=n.onchange,window.setTimeout(function(){t(n.value)},1))},function(e,t){var n="TEXTAREA"==e.tagName.toUpperCase()||"SELECT"==e.tagName.toUpperCase(),i="INPUT"==e.tagName.toUpperCase(),r="name"in e,o="",a=t,c=!1;if(r&&(n||i)){for(o=e.name,kDot=o.indexOf(".");0<kDot;){var s=o.substring(0,kDot);s in a||(a[s]={}),a=a[s],o=o.substring(kDot+1),kDot=o.indexOf(".")}(c=o.endsWith("[]"))&&(o=o.substring(0,o.length-2))}if(n)a[o]=e.value;else if(i){n="type"in e?e.type.toUpperCase():"text";"SUBMIT"!=n&&"RESET"!=n&&r&&(c?(o in a&&(a[o]=[]),a=a[o],"CHECKBOX"==n?a.push(!!e.checked):"RADIO"==n&&!e.checked||a.push(e.value)):"CHECKBOX"==n?a[o]=!!e.checked:"RADIO"==n&&!e.checked||(a[o]=e.value))}else if("children"in e)for(var u=e.children.length,d=0;d<u;d++){var l=e.children[d];M(l,t)}}),E=function(e){var t={};return M(e,t),t},T=function(n,i){return function(e){var t={},e=(t[i]=e,c(n.view,t));n.view.tree.update(e)}},N={},O={},L=(S.PRWP=function(e,t){if(N[e]=t,e in O)for(var n=O[e],i=0;i<n.length;i++)n[i]()},S.WP=function(e,t,n,i){var r;n in N?N[n](e,t,i,S):(r=function(){N[n](e,t,i,S)},n in O?O[n].push(r):O[n]=[r])},S.PG=function(e,t){for(var n=C,i=0;i<e.length;i++){var r=e[i];r in n||(n[r]={}),n=n[r]}n["@"]=t},function(e,t,n,i){if(t<e.length){if("number"in n){var r=n.number,o=parseFloat(e[t]);if(!isNaN(o))for(var a in r){if(i[a]=o,null!==(c=L(e,t+1,r[a],i)))return c;delete i[a]}}if("text"in n){r=n.text,o=e[t];for(a in r){if(i[a]=o,null!==(c=L(e,t+1,r[a],i)))return c;delete i[a]}}var c;if("fixed"in n)for(a in r=n.fixed)if(a==e[t])if(null!==(c=L(e,t+1,r[a],i)))return c}else if("@"in n)return n["@"]}),P=(S.goto=function(e,t){window.setTimeout(function(){t.startsWith("/")?S.run(document.body,t,!0):window.location.href=o(t)},10)},S.init=function(){S.run(document.body,i(window.location.pathname+window.location.hash),!1),window.onpopstate=function(){S.run(document.body,i(window.location.pathname+window.location.hash),!1)}},S.run=function(e,t,n){for(conKey in q)q[conKey].tree.nuke();var i=(t.startsWith("/")?t.substring(1):t).split("/"),r={session_id:"R"+Math.random()},i=L(i,0,C,r);h(e),null!=i?(i(e,i={service:A,data:null,view:a(e),current:"view"}),i.view.tree.subscribe(i.view.delta),i.view.tree.update(r),n&&window.history.pushState({},"",o(t))):"/404"!=t&&S.run(e,"/404"),e.appendChild(D)},{}),R=(S.SIGNOUT=function(){P={},localStorage.removeItem("identity_default");var e,t=[];for(e in q){var n=q[e];null!=n.ptr&&n.ptr.end({success:function(){},failure:function(){}}),t.push(e)}for(var i=0;i<t.length;i++)delete q[t[i]];S.goto(null,"/")},S.GOOGLE_SIGN_ON=function(e){A.InitConvertGoogleUser(e,{success:function(e){P.default=e.identity,localStorage.setItem("identity_default",e.identity),S.goto(null,"/")},failure:function(e){console.log("Google failure: "+e)}})},S.ID=function(e,t){!0===e&&(e="default");var n=null,i=function(){},r=localStorage.getItem("identity_"+e);if(r&&(P[e]=r),e.startsWith("direct:"))n=e.substring(7);else{if(!(e in P))return window.setTimeout(function(){S.goto(null,t)},10),{abort:!0};n=P[e],i=function(){delete P[e],localStorage.removeItem("identity_"+e),S.goto(null,t)}}return{abort:!1,cleanup:i,identity:n}},S.FIDCL=function(t,n){return{success:function(e){t.success(e)},next:function(e){t.next(e)},complete:function(){t.complete()},failure:function(e){t.failure(e),966671==e&&n.cleanup()}}},{});S.PRCUDA=function(e,t){R[e]=t},S.CUDA=function(n,i,r,o,a,c){var s=p();o.__=u(10,function(){v(s),h(n);var e=!1,t=(r in R&&("function"==typeof(t=R[r])&&(e=t(o,i,a,S))),e=e||new AdamaTree,{service:i.service,data:{connection:i.connection,tree:e,delta:{},parent:null,path:null},view:d(i.view),current:"data"});c(t),m(t,s)})},S.ST=function(e){document.title=e.value,e.__=u(1,function(){document.title=e.value})},S.CONNECT=function(r,o,a){var c={view:function(){}};o.__=u(5,function(){var t,e,n,i;"key"in o&&"space"in o&&"name"in o&&(t=s(o.name),n=o.space+"/"+o.key,e=function(e){c.view=r.view.tree.subscribe(function(){null!=t.ptr&&t.ptr.update(r.view.tree.copy(),{success:function(){},failure:function(){}})}),e&&t.ptr.update(r.view.tree.copy(),{success:function(){},failure:function(){}})},null!=t.ptr&&t.bound==n?e(!0):(t.space=o.space,t.key=o.key,null!=t.ptr&&(t.ptr.end({success:function(){},failure:function(){}}),t.ptr=null),t.bound=n,(n=S.ID(o.identity,a)).abort||(i=n.identity,n.cleanup,c.view(),t.ptr=A.ConnectionCreate(i,o.space,o.key,r.view.tree.copy(),{next:function(e){t.set_connected(!0),"data"in e.delta&&t.tree.update(e.delta.data),"outstanding"in e.delta&&t.ondecide(e.delta.outstanding)},complete:function(){t.set_connected(!1)},should_retry:!0,retry_task_name:"Document Connection:"+o.space+"/"+o.key,failure:function(e){console.log("CONNECT FAILURE:"+o.space+"/"+o.key+" ["+o.name+"] "+e),t.set_connected(!1),t.ptr=null}}),t.tree.update({}),e(!1))))})},S.INTERNAL=function(e){return{service:e.service,data:{connection:null,tree:new AdamaTree,delta:{},parent:null,path:null},view:d(e.view),current:"data"}};return S.aUP=function(e,t,n,i,r){var n=S.ID(n,r);n.abort||(e.action="https://aws-us-east-2.adama-platform.com/~upload",e.method="post",e.enctype="multipart/form-data",(r=document.createElement("input")).type="hidden",r.name="identity",r.value=n.identity,e.appendChild(r),(n=document.createElement("iframe")).name="UPLOAD_"+Math.random(),n.width="1",n.height="1",e.appendChild(n),e.target=n.name)},S.aSO=function(t,n,i,e,r){var o=T(n,e);window.setTimeout(function(){x(t)},1),t.onsubmit=function(e){e.preventDefault();e=E(t);e.remember?localStorage.setItem("email_remember",e.email):localStorage.setItem("email_remember",""),A.AccountLogin(e.email,e.password,{success:function(e){o(!1),P[i]=e.identity,localStorage.setItem("identity_"+i,e.identity),S.goto(n.view,r),w(t)},failure:function(e){o(!0),console.log("Sign in failure:"+e),b(t)}})}},S.aSU=function(n,i,e,r){var o=T(i,e);n.onsubmit=function(e){e.preventDefault();var t=E(n);A.InitSetupAccount(t.email,{success:function(e){o(!1),localStorage.setItem("email",t.email),S.goto(i.view,r),w(n)},failure:function(e){o(!0),console.log("Sign up failure:"),b(n)}})}},S.aSP=function(i,r,e,o){var a=T(r,e);i.onsubmit=function(e){e.preventDefault();var n=E(i);"email"in n||(n.email=localStorage.getItem("email")),A.InitCompleteAccount(n.email,!1,n.code,{success:function(e){var t=e.identity;A.AccountSetPassword(e.identity,n.password,{success:function(){a(!1),localStorage.setItem("identity_default",t),S.goto(r.view,o),w(i)},failure:function(e){a(!0),b(i)}})},failure:function(e){a(!0),b(i)}})}},S.aSD=function(n,i,r,e){var o=T(i,e);n.onsubmit=function(e){e.preventDefault();var t=performance.now();i.data.connection.ptr.send(r,E(n),{success:function(e){o(!1),w(n),console.log("Success|"+e.seq+";latency="+(performance.now()-t))},failure:function(e){o(!0),console.log("Send failure:"+e),b(n)}})}},window.rxhtml=S}();
