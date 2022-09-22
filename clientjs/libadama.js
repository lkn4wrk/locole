function AdamaTree(){function i(e){if(Array.isArray(e))for(var t=[],n=0;n<e.length;n++)t.push(i(e[n]));else{if("object"!=typeof e)return e;t={};for(key in e)"#"!=key[0]&&"__key"!=key&&"@o"!=key&&(t[key]=i(e[key]))}return t}function D(e,t){if(0==e.length)return e;for(var n=[],i=0;i<e.length;i++)t in e[i]&&n.push(e[i][t]);return n}function N(e,t,n){for(var i=0;i<e.length;i++){var r=e[i];if("@e"in r)for(var o=r["@e"],a=0;a<o.length;a++)n.push(function(){this.f(this.v)}.bind({f:o[a],v:t}))}}function L(e,t,n,i){for(var r in t){var o="#"+r,a=e[r],c=t[r];if(null==c)Array.isArray(a)&&(o in e&&U(D(n,o)),delete e[o]),U(D(n,r)),delete e[r];else if("object"!=typeof c||Array.isArray(c))e[r]=c,N(D(n,r),c,i);else if(Array.isArray(a)||"@o"in c||"@s"in c){r in e||(e[r]=[]),o in e||(e[o]={},"@o"in c&&(e[o]["@o"]=!0),e[o].__key=r);var u,s=e[o],d=D(n,r),l=null,f=null,h={};for(u in c){var p=c[u];if("@o"==u)l=p;else if("@s"==u)f=p;else if(null==p)u in s&&(h[u]=function(e){U(D(d,e)),delete s[e],O(d,e);for(var t=d,n=e,i=0;i<t.length;i++){var r=t[i];if("-"in r){r=r["-"];if("@e"in r)for(var o=r["@e"],a=0;a<o.length;a++)o[a](n)}}});else{if(!(u in s)){b=w=g=y=m=void 0;for(var v=d,_=u,m=0;m<v.length;m++){var y=v[m];if("+"in y){var g=y["+"];if("@e"in g)for(var w=g["@e"],b=0;b<w.length;b++)P(y,w[b](_),_)}}}p=c[u];"object"==typeof p?(u in s||(s[u]={},s[u].__key=u),h[u]=function(e){L(s[e],c[e],D(d,e),i),N(D(d,e),s[e],i)}):(s[u]=p,h[u]=function(e){N(D(d,e),s[e],i)})}}var k=e[r],x=[];if(null!=l){for(var S=[],A=[],C=0;C<l.length;C++){var q=l[C],I=typeof q;if("string"==I||"number"==I)S.push(s[q]),A.push(""+q);else for(var I=q[0],T=q[1],E=I;E<=T;E++)S.push(k[E]),A.push(k[E].__key)}x.push(function(){N(D(d,"~"),A,i)}),e[r]=S}else if(null!=f){for(S=[],A=[],C=0;C<f;C++)S[C]=k[C],A.push(""+C);x.push(function(){N(D(d,"~"),A,i)}),e[r]=S}for(u in h)h[u](u);for(C=0;C<x.length;C++)x[C]();N(d,e[r],i)}else{r in e&&"object"==typeof e[r]||(e[r]={});a=D(n,r);L(e[r],c,a,i),N(a,e[r],i)}}N(n,e,i)}function r(e){for(var t=0;t<e.length;t++)e[t]()}function o(e,t){if(Array.isArray(e))for(var n=0;n<e.length;n++)o(e[n],t);else if("object"==typeof e)for(var i in e)"@e"==i?o(e[i],t):(i in t||(t[i]={}),o(e[i],t[i]));else"function"==typeof e&&("@e"in t?t["@e"].push(e):t["@e"]=[e])}function u(e){var t,n={};for(t in e)if("#"!=t[0]&&"@o"!=t&&"__key"!=t){var i=e[t];if("#"+t in e){var r={};if("@o"in e["#"+t]){for(var o=[],a=0;a<i.length;a++){var c=i[a];r[c.__key]=u(c),o.push(i[a].__key)}r["@o"]=o}else{for(a=0;a<i.length;a++)r[""+a]=u(i[a]);r["@s"]=i.length}n[t]=r}else n[t]="object"==typeof i?u(i):i}return n}var a=0,c={},s={},P=(this.nuke=function(){s={}},this.copy=function(){return i(c)},this.str=function(){return JSON.stringify(c)},function(e,t,n){if(Array.isArray(t))for(var i=0;i<t.length;i++)P(e,t[i],n);else if("function"==typeof t)n in e||(e[n]={}),"@e"in(o=e[n])?o["@e"].push(t):o["@e"]=[t];else if("object"==typeof t){n in e||(e[n]={});var r,o=e[n];for(r in t)"@e"==r?P(e,t[r],n):P(o,t[r],r)}}),U=function(e){if(Array.isArray(e))for(var t=e.length,n=0;n<t;n++)U(e[n]);else if("object"==typeof e)for(var i in e){var r=e[i];if("@e"==i)for(var o=0;o<r.length;o++)r[o](null);else U(r)}},O=function(e,t){if(Array.isArray(e))for(var n=e.length,i=0;i<n;i++)O(e[i],t);else"object"==typeof e&&t in e&&delete e[t]};this.update=function(e){var t,n=[];for(t in s)n.push(s[t]);var i=[];L(c,e,n,i),r(i)};this.subscribe=function(e){var t={},e=(o(e,t),u(c)),n=[],i=(L({},e,[t],n),r(n),""+a++);return s[i]=t,function(){delete s[i]}}}class WebSocketAdamaConnection{constructor(e){this.backoff=1,this.host=e,this.url="wss://"+e+"/~s",this.connected=!1,this.assets=!0,this.dead=!1,this.maximum_backoff=5e3,this.reset_backoff=1e3,this.socket=null,this.onstatuschange=function(e){},this.onping=function(e,t){},this.scheduled=!1,this.callbacks=new Map,this.nextId=0,this.onreconnect=new Map}stop(){this.dead=!0,null!==this.socket&&this.socket.close()}_retry(){var e,t;this.socket=null,this.connected&&(this.connected=!1,this.onstatuschange(!1)),this.callbacks.clear(),this.dead||this.scheduled||(e=!1,this.backoff+=Math.random()*this.backoff,this.backoff>this.maximum_backoff&&(this.backoff=this.maximum_backoff,e=!0),this.scheduled=!0,t=this,setTimeout(function(){t.start()},this.backoff),e&&(this.backoff/=2))}start(){var n=this;this.scheduled=!1,this.dead=!1,this.socket=new WebSocket(this.url),this.socket.onmessage=function(e){var t,e=JSON.parse(e.data);{if(!("ping"in e))return"status"in e?"connected"!=e.status?(n.socket.close(),n.socket=null,n.backoff=n.reset_backoff,void n._retry()):(n.backoff=1,n.connected=!0,n.assets=e.assets,n.onstatuschange(!0),n._reconnect(),void n.ConfigureMakeOrGetAssetKey({success:function(e){try{var t=new XMLHttpRequest;t.open("GET","https://"+n.host+"/~p"+e.assetKey,!0),t.withCredentials=!0,t.send()}catch(e){console.log(e)}},failure:function(){}})):void("failure"in e?n.callbacks.has(e.failure)&&(t=n.callbacks.get(e.failure))&&(n.callbacks.delete(e.failure),t(e)):"deliver"in e&&n.callbacks.has(e.deliver)&&(t=n.callbacks.get(e.deliver))&&(e.done&&n.callbacks.delete(e.deliver),t(e)));n.onping(e.ping,e.latency),e.pong=(new Date).getTime()/1e3,n.socket.send(JSON.stringify(e))}},this.socket.onclose=function(e){n._retry()},this.socket.onerror=function(e){n._retry()}}_write(e,t){this.connected?(this.callbacks.set(e.id,t),this.socket.send(JSON.stringify(e))):t({failure:600,reason:9999})}async wait_connected(){var n,i;return this.connected?new Promise(function(e){e(!0)}):(i=(n=this).onstatuschange,new Promise(function(t){n.onstatuschange=function(e){i(e),e&&(t(!0),n.onstatuschange=i)}}))}_reconnect(){this.onreconnect.forEach(function(e,t){e.__retry()})}__execute_rr(t){var n=this;return t.first=!0,n._write(t.request,function(e){t.first&&(t.first=!1,"failure"in e?"failure"in t.responder&&t.responder.failure(e.reason):"success"in t.responder&&t.responder.success(e.response)),n.onreconnect.delete(t.id)}),n.onreconnect.set(t.id,t),t.__retry=function(){n.__execute_rr(t)},t}__execute_stream(t){var n=this;return n._write(t.request,function(e){"failure"in e?("failure"in t.responder&&t.responder.failure(e.reason),n.onreconnect.delete(t.id)):(e.response&&"next"in t.responder&&t.responder.next(e.response),e.done&&("complete"in t.responder&&t.responder.complete(),n.onreconnect.delete(t.id)))}),n.onreconnect.set(t.id,t),t.__retry=function(){n.__execute_stream(t)},t}__id(){return this.nextId++,this.nextId}InitSetupAccount(e,t){var n=this.__id();return this.__execute_rr({id:n,responder:t,request:{method:"init/setup-account",id:n,email:e}})}InitConvertGoogleUser(e,t){var n=this.__id();return this.__execute_rr({id:n,responder:t,request:{method:"init/convert-google-user",id:n,"access-token":e}})}InitCompleteAccount(e,t,n,i){var r=this.__id();return this.__execute_rr({id:r,responder:i,request:{method:"init/complete-account",id:r,email:e,revoke:t,code:n}})}AccountSetPassword(e,t,n){var i=this.__id();return this.__execute_rr({id:i,responder:n,request:{method:"account/set-password",id:i,identity:e,password:t}})}AccountGetPaymentPlan(e,t){var n=this.__id();return this.__execute_rr({id:n,responder:t,request:{method:"account/get-payment-plan",id:n,identity:e}})}AccountLogin(e,t,n){var i=this.__id();return this.__execute_rr({id:i,responder:n,request:{method:"account/login",id:i,email:e,password:t}})}Probe(e,t){var n=this.__id();return this.__execute_rr({id:n,responder:t,request:{method:"probe",id:n,identity:e}})}AuthorityCreate(e,t){var n=this.__id();return this.__execute_rr({id:n,responder:t,request:{method:"authority/create",id:n,identity:e}})}AuthoritySet(e,t,n,i){var r=this.__id();return this.__execute_rr({id:r,responder:i,request:{method:"authority/set",id:r,identity:e,authority:t,"key-store":n}})}AuthorityGet(e,t,n){var i=this.__id();return this.__execute_rr({id:i,responder:n,request:{method:"authority/get",id:i,identity:e,authority:t}})}AuthorityList(e,t){var n=this.__id();return this.__execute_stream({id:n,responder:t,request:{method:"authority/list",id:n,identity:e}})}AuthorityDestroy(e,t,n){var i=this.__id();return this.__execute_rr({id:i,responder:n,request:{method:"authority/destroy",id:i,identity:e,authority:t}})}SpaceCreate(e,t,n,i){var r=this.__id();return this.__execute_rr({id:r,responder:i,request:{method:"space/create",id:r,identity:e,space:t,template:n}})}SpaceGenerateKey(e,t,n){var i=this.__id();return this.__execute_rr({id:i,responder:n,request:{method:"space/generate-key",id:i,identity:e,space:t}})}SpaceUsage(e,t,n,i){var r=this.__id();return this.__execute_stream({id:r,responder:i,request:{method:"space/usage",id:r,identity:e,space:t,limit:n}})}SpaceGet(e,t,n){var i=this.__id();return this.__execute_rr({id:i,responder:n,request:{method:"space/get",id:i,identity:e,space:t}})}SpaceSet(e,t,n,i){var r=this.__id();return this.__execute_rr({id:r,responder:i,request:{method:"space/set",id:r,identity:e,space:t,plan:n}})}SpaceSetRxhtml(e,t,n,i){var r=this.__id();return this.__execute_rr({id:r,responder:i,request:{method:"space/set-rxhtml",id:r,identity:e,space:t,rxhtml:n}})}SpaceGetRxhtml(e,t,n){var i=this.__id();return this.__execute_rr({id:i,responder:n,request:{method:"space/get-rxhtml",id:i,identity:e,space:t}})}SpaceDelete(e,t,n){var i=this.__id();return this.__execute_rr({id:i,responder:n,request:{method:"space/delete",id:i,identity:e,space:t}})}SpaceSetRole(e,t,n,i,r){var o=this.__id();return this.__execute_rr({id:o,responder:r,request:{method:"space/set-role",id:o,identity:e,space:t,email:n,role:i}})}SpaceReflect(e,t,n,i){var r=this.__id();return this.__execute_rr({id:r,responder:i,request:{method:"space/reflect",id:r,identity:e,space:t,key:n}})}SpaceList(e,t,n,i){var r=this.__id();return this.__execute_stream({id:r,responder:i,request:{method:"space/list",id:r,identity:e,marker:t,limit:n}})}DomainMap(e,t,n,i,r){var o=this.__id();return this.__execute_rr({id:o,responder:r,request:{method:"domain/map",id:o,identity:e,domain:t,space:n,certificate:i}})}DomainUnmap(e,t,n){var i=this.__id();return this.__execute_rr({id:i,responder:n,request:{method:"domain/unmap",id:i,identity:e,domain:t}})}DomainGet(e,t,n){var i=this.__id();return this.__execute_rr({id:i,responder:n,request:{method:"domain/get",id:i,identity:e,domain:t}})}DocumentCreate(e,t,n,i,r,o){var a=this.__id();return this.__execute_rr({id:a,responder:o,request:{method:"document/create",id:a,identity:e,space:t,key:n,entropy:i,arg:r}})}DocumentDelete(e,t,n,i){var r=this.__id();return this.__execute_rr({id:r,responder:i,request:{method:"document/delete",id:r,identity:e,space:t,key:n}})}DocumentList(e,t,n,i,r){var o=this.__id();return this.__execute_stream({id:o,responder:r,request:{method:"document/list",id:o,identity:e,space:t,marker:n,limit:i}})}ConnectionCreate(e,t,n,i,r){var u=this,s=u.__id();return u.__execute_stream({id:s,responder:r,request:{method:"connection/create",id:s,identity:e,space:t,key:n,"viewer-state":i},send:function(e,t,n){var i=u.__id();u.__execute_rr({id:i,responder:n,request:{method:"connection/send",id:i,connection:s,channel:e,message:t}})},sendOnce:function(e,t,n,i){var r=u.__id();u.__execute_rr({id:r,responder:i,request:{method:"connection/send-once",id:r,connection:s,channel:e,dedupe:t,message:n}})},canAttach:function(e){var t=u.__id();u.__execute_rr({id:t,responder:e,request:{method:"connection/can-attach",id:t,connection:s}})},attach:function(e,t,n,i,r,o,a){var c=u.__id();u.__execute_rr({id:c,responder:a,request:{method:"connection/attach",id:c,connection:s,"asset-id":e,filename:t,"content-type":n,size:i,"digest-md5":r,"digest-sha384":o}})},update:function(e,t){var n=u.__id();u.__execute_rr({id:n,responder:t,request:{method:"connection/update",id:n,connection:s,"viewer-state":e}})},end:function(e){var t=u.__id();u.__execute_rr({id:t,responder:e,request:{method:"connection/end",id:t,connection:s}})}})}ConfigureMakeOrGetAssetKey(e){var t=this.__id();return this.__execute_rr({id:t,responder:e,request:{method:"configure/make-or-get-asset-key",id:t}})}AttachmentStart(e,t,n,i,r,o){var a=this,c=a.__id();return a.__execute_stream({id:c,responder:o,request:{method:"attachment/start",id:c,identity:e,space:t,key:n,filename:i,"content-type":r},append:function(e,t,n){var i=a.__id();a.__execute_rr({id:i,responder:n,request:{method:"attachment/append",id:i,upload:c,"chunk-md5":e,"base64-bytes":t}})},finish:function(e){var t=a.__id();a.__execute_rr({id:t,responder:e,request:{method:"attachment/finish",id:t,upload:c}})}})}}var Adama={Production:"aws-us-east-2.adama-platform.com",Connection:WebSocketAdamaConnection},RxHTML=function(){function o(e){return e.startsWith("/")?A+e.substring(1):e}function u(e){var d;return e in x?x[e]:((d={name:e,ptr:null,tree:new AdamaTree,outstanding:{},decisions:{},choice_subs:{},resets:{},connection_events:{},id:0,connection_state:!1,choices:{}}).set_connected=function(e){if(this.connection_state!=e){this.connection_state=e;var t,n=[];for(t in d.connection_events)d.connection_events[t](e)||n.push(t);for(var i=0;i<n.length;i++)delete d.connection_events[n[i]]}}.bind(d),d.connected=function(e){var t="-|"+this.id++;return(this.connection_events[t]=e)(this.connection_state),function(){delete this.connection_events[t]}.bind(this)}.bind(d),d.subscribe_any=function(e){var t="-|"+this.id++;return this.decisions[t]=e,function(){delete this.decisions[t]}.bind(this)}.bind(d),d.subscribe=function(e,t){var n=e+"|"+this.id++;return this.decisions[n]=t,function(){delete this.decisions[n]}.bind(this)}.bind(d),d.subscribe_reset=function(e){var t="reset|"+this.id++;return this.resets[t]=e,function(){delete this.resets[t]}.bind(this)}.bind(d),d.subscribe_choice=function(e,t){var n=e+"|"+this.id++;return this.choice_subs[n]=t,function(){delete this.choice_subs[n]}.bind(this)}.bind(d),d.onchoices=function(e,t){var n,i=[];for(n in d.choice_subs)!n.startsWith(e+"|")||d.choice_subs[n](t)||i.push(n);for(var r=0;r<i.length;r++)delete d.choice_subs[i[r]]},d.ondecide=function(e){var t,n=[];for(t in d.resets)(0,d.resets[t])()||n.push(t);for(var i=0;i<n.length;i++)delete d.resets[n[i]];for(r in d.outstanding)d.outstanding[r]={options:[]};for(var r,o=e.length,i=0;i<o;i++){var a=e[i];d.outstanding[a.channel]=a}for(r in d.outstanding){var c,u=d.outstanding[r],s=[];for(c in d.decisions)!c.startsWith(r+"|")&&!c.startsWith("-|")||d.decisions[c](u,r)||s.push(c);for(i=0;i<s.length;i++)delete d.decisions[s[i]]}},x[e]=d)}function f(e,t,n){e=b.pI(e,t),"@e"in(t=e[e.current]).delta?t.delta["@e"].push(n):t.delta["@e"]=[n]}function d(e){if(null==e)return null;var t=null,n={};return null!=(t=null!=e.parent?d(e.parent):t)&&(t.delta[e.path]=n),{tree:e.tree,parent:t,delta:n,path:e.path}}function l(e){for(var t=e;null!=t.parent;)t=t.parent;return t}function h(e){for(var t=e.lastChild;t;)e.removeChild(t),t=e.lastChild}function s(e,t){var n={inflight:!1,timeout:null,inflight:!1};return function(){n.inflight||(n.inflight=!0,n.timeout=window.setTimeout(function(){n.inflight=!1,n.timeout=null,t()},e))}}function p(){return{__data:function(){},__view:function(){}}}function v(e){e.__data=function(){},e.__view=function(){}}function _(e){e.__data(),e.__view()}function m(e,t){null!=e.data?t.__data=e.data.tree.subscribe(l(e.data).delta):t.__data=function(){},null!=e.view?t.__view=e.view.tree.subscribe(l(e.view).delta):t.__view=function(){}}function y(e){var t={service:e.service,data:d(e.data),view:d(e.view),current:e.current};return null!=t.data&&(t.data.connection=e.data.connection),t}function g(e,t,n,i){if(t in e.data.connection.outstanding)for(var r=e.data.connection.outstanding[t].options,o=0;o<r.length;o++){var a=r[o];if(n in a&&a[n]==i)return a}return null}function w(e,t){var n,e=e.data.connection.choices,i=(t in e||(e[t]={}),e[t]),r=[];for(n in i)r.push(i[n]);return r}function a(e){if("INPUT"==e.tagName.toUpperCase())"email"==e.type&&"email"==e.name&&(e.value=localStorage.getItem("email_remember"));else if("children"in e)for(var t=e.children.length,n=0;n<t;n++)a(e.children[n])}var e,t,b={},r={},c={},k=new Adama.Connection(Adama.Production),x={},S=document.createElement("div"),n=document.createElement("span"),i=document.createElement("span"),A=(k.onstatuschange=function(e){i.innerHTML=e?"Yes":"No",e?k.onping=function(e,t){1<=t&&(n.innerHTML=""+t)}:(n.innerHTML="",k.onping=function(e,t){})},S.appendChild(i),S.appendChild(n),S.style="position:fixed; bottom:0px; right:0px",k.start(),"/"),C=function(e){return e},q=(window.location.hostname.endsWith(".adama-platform.com")&&!window.location.hostname.endsWith("ide.adama-platform.com")&&(e=window.location.pathname.split("/"),A=[e[0],e[1],e[2],""].join("/"),t=e[0].length+e[1].length+e[2].length+2,C=function(e){return e.substring(t)}),b.make=function(){return new AdamaTree},function(e,t){var n;return null!=e.parent?((n=q(e.parent,{}))[e.path]=t,n):t}),I=(b.pV=function(e){return{service:e.service,data:e.data,view:e.view,current:"view"}},b.pD=function(e){return{service:e.service,data:e.data,view:e.view,current:"data"}},b.pR=function(e){for(var t={service:e.service,data:e.data,view:e.view,current:e.current},n=t[e.current];null!=n.parent;){if(null==n.parent)return t[e.current],n;n=n.parent}return t[e.current]=n,t},b.pU=function(e){var t={service:e.service,data:e.data,view:e.view,current:e.current},n=t[e.current];return null!=n.parent&&(t[e.current]=n),t},b.pI=function(e,t){var n=e[e.current],i=(t in n.delta||(n.delta[t]={}),{service:e.service,data:e.data,view:e.view,current:e.current});return i[e.current]={tree:n.tree,delta:n.delta[t],parent:n,path:t},"data"==i.current&&(i.data.connection=n.connection),i},b.pEV=function(e,t){return t in e.view.delta||(e.view.delta[t]={}),{service:e.service,data:e.data,view:{tree:e.view.tree,delta:e.view.delta[t],parent:e.view,path:t},current:e.current}},b.Y=function(e,t,n,i){f(e,n,function(e){t[n]=e,i()})},b.Y2=function(e,n,i,r,o){f(e,r,function(e){var t=n._[i];return t[r]=e,o(t),!0})},b.RX=function(e){for(var t={_:{}},n=0;n<e.length;n++)t._[e[n]]={};return t},b.YS=function(e,t,n){f(e,n,function(e){return t[n]=e,!0})},b.T=function(e){return document.createTextNode(e)},b.L=function(e,t){var n=document.createTextNode("");return f(e,t,function(e){n.nodeValue=e}),n},b.LT=function(e,t,n){var i=document.createTextNode("");return f(e,t,function(e){null!=e&&(i.nodeValue=n(e))}),i},b.E=function(e,t){return null==t?document.createElement(e):((e=document.createElementNS(t,e)).setAttribute("xmlns",t),e)},b.P=function(i,r,e,o,a){var c=p();e.__=function(){var n;"name"in e&&this.name!=e.name&&(n=u(e.name),this.name=e.name,n.connected(function(e){h(i),_(c);var t={service:r.service,data:{connection:n,tree:n.tree,delta:{},parent:null,path:null},view:d(r.view),current:"data"};return(e?o:a)(i,t),m(t,c),!0}))}.bind({name:""})},b.TP=function(e,t){r[e]=t},b.HREF=function(e,n){e.setAttribute("href",o(n)),e.onclick=function(e){var t=(n.startsWith("/")?n.substring(1):n).split("/");return!L(t,0,c,{})||(e.preventDefault(),b.run(document.body,n,!0),!1)}},b.ACLASS=function(e,t){e.setAttribute("class",t)},b.ASRC=function(e,t){e.setAttribute("src",t)},b.UT=function(e,t,n,i){(0,r[n])(e,t,i)},b.SW=function(n,i,e,r){var t={prior:null},t=(v(t),function(e){var t;e!=this.prior&&(this.prior=e,_(this),h(n),t=y(i),r(n,t,""+e),m(t,this))}.bind(t));f(i,e,t)},b.IT=function(r,e,t,o,a){var c=b.pI(e,t),u={},s={};f(e,t,{"+":function(e){var t=b.pI(c,e),n=(t={service:(t=o?b.pEV(c,e):t).service,data:t.data,view:d(t.view),current:t.current},p()),i=a(t);return u[e]=i,s[e]=n,r.append(i),e=n,null!=(i=t).view?e.__view=i.view.tree.subscribe(l(i.view).delta):e.__view=function(){},t[t.current].delta},"-":function(e){e in u&&(r.removeChild(u[e]),delete u[e]),e in s&&(_(s[e]),delete s[e])},"~":function(e){h(r);for(var t=0;t<e.length;t++)r.append(u[e[t]])}})},{}),O=(b.PRCUAC=function(e,t){I[e]=t},b.exCC=function(e,t,n,i){e.addEventListener(t,function(){i in I&&I[i]()})},b.aCC=function(t,n,i,e){var r=E(n,e);t.onsubmit=function(e){i in I&&(e.preventDefault(),e=T(t),I[i](e,n,r,b))}},b.exFIN=function(e,t,n,i){e.addEventListener(t,function(){function t(){delete n.data.connection.choices[i],n.data.connection.onchoices(i,{})}var e=w(n,i);n.data.connection.ptr.send(i,e,{failure:function(e){t(),console.log("failed:"+e)},success:function(e){t(),console.log("Success|"+e.seq)}})})},b.FIN=function(n,i,r,o,e,a,c){var u={owner:n,shown:!1};v(u);u.update=function(){var e,t=i.data.connection.outstanding[r];t&&(e=w(i,r),e=!("min"in t&&"max"in t)||t.min<=e.length&&e.length<=t.max,u.eval!=e&&(u.eval=e,t=u.eval,_(u),h(n),e=y(i),(t===o?a:c)(n,e),m(e,u)))},i.data.connection.subscribe_choice(r,function(){return u.update(),!0})},b.exCH=function(e,t,i,n,r,o){var a={value:null};e.addEventListener(t,function(){var e,t,n=g(i,r,o,a.value);null!=n&&(e=i.data.connection.choices,r in e||(e[r]={}),e=e[r],(t=n[o])in e?delete e[t]:e[t]=n,i.data.connection.onchoices(r,e))}),f(i,n,function(e){a.value=e})},b.exD=function(e,t,n,i,r,o){var a={value:null};e.addEventListener(t,function(){var e=g(n,r,o,a.value);if(null!=e){let t=performance.now();n.data.connection.ptr.send(r,e,{failure:function(e){},success:function(e){console.log("Success|"+e.seq+";latency="+(performance.now()-t))}})}}),f(n,i,function(e){a.value=e})},b.onS=function(e,t,n,i,r){function o(){(e={})[i]="function"==typeof r?r():r;var e=q(n,e);n[n.current].tree.update(e)}"load"==t?window.setTimeout(o,1):e.addEventListener(t,o)},b.onT=function(e,t,n,i){var r={value:!1};e.addEventListener(t,function(){var e={},e=(e[i]=!r.value,q(n,e));n[n.current].tree.update(e)}),f(n,i,function(e){r.value=1==e})},b.onD=function(e,t,n,i,r){var o={value:0};e.addEventListener(t,function(){var e={},e=(e[i]=o.value+r,q(n,e));n[n.current].tree.update(e)}),f(n,i,function(e){"number"==typeof e?o.value=e:(e=parseFloat(e),isNaN(e)||(o.value=e))})},b.CSEN=function(n,i,e,r,t,o,a,c,u,s){var d={value:"",owner:n,shown:!1,eval:null};v(d);d.update=function(){var e,t;i.data.connection.outstanding[r]&&(e=i.data.connection.choices,r in e||(e[r]={}),e=e[r],e=d.value in e,d.eval!=e&&(d.eval=e,e=d.eval,_(d),h(n),t=y(i),(e===a?u:s)(n,t),m(t,d)))},i.data.connection.subscribe_choice(r,function(){return d.update(),!0}),f(e,o,function(e){d.value=e,d.update()})},b.DE=function(n,i,e,r,o,t,a,c,u,s){var d={value:"",owner:n,shown:!1,eval:null};v(d);d.update=function(){var e,t=null!=g(i,r,o,d.value);d.eval!=t&&(d.eval=t,t=d.eval,_(d),h(n),e=y(i),(t===a?u:s)(n,e),m(e,d))},i.data.connection.subscribe(r,function(){return d.update(),!0}),f(e,t,function(e){d.value=e,d.update()})},b.IF=function(r,o,a,c,u,s,d){var l=p(),e=function(e){var t,n,i=!!e===c;this.shown!=i&&(this.shown=i,h(r),_(l),n=t=y(o),"object"==typeof e&&(n=b.pI(n,a),u&&(n=b.pEV(n,a))),(i?s:d)(r,n),m(t,l))}.bind({shown:c});e(!c),f(o,a,e)},b.aCP=function(n,i,r){n.onsubmit=function(e){e.preventDefault();var e=T(n),t=("."!=r&&""!=r&&((t={})[r]=e,e=t),q(i,e));i.view.tree.update(t)}},b.SY=function(n,i,r,e){function t(e){(t={})[r]=n.value;var t=q(i,t);i.view.tree.update(t)}var o="type"in n?n.type.toUpperCase():"text";"CHECKBOX"==o?n.onchange=s(e,function(e){t(n.checked)}):"RADIO"==o?n.onchange=s(e,function(e){n.checked&&t(n.value)}):(n.onchange=s(e,function(e){t(n.value)}),n.onkeyup=n.onchange,window.setTimeout(function(){t(n.value)},1))},function(e,t){if("TEXTAREA"==e.tagName.toUpperCase())t[e.name]=e.value;else if("SELECT"==e.tagName.toUpperCase())t[e.name]=e.value;else if("INPUT"==e.tagName.toUpperCase()){var n,i,r="type"in e?e.type.toUpperCase():"text";"SUBMIT"!=r&&"RESET"!=r&&"name"in e&&(i=t,(n=e.name).endsWith("[]")?((n=n.substr(0,n.length-2))in i&&(i[n]=[]),i=i[n],"CHECKBOX"==r?i.push(!!e.checked):"RADIO"==r&&!e.checked||i.push(e.value)):"CHECKBOX"==r?i[n]=!!e.checked:"RADIO"==r&&!e.checked||(i[n]=e.value))}else if("children"in e)for(var o=e.children.length,a=0;a<o;a++){var c=e.children[a];O(c,t)}}),T=function(e){var t={};return O(e,t),t},E=function(n,i){return function(e){var t={},e=(t[i]=e,q(b.pV(n),t));n.view.tree.update(e)}},D={},N={},L=(b.PRWP=function(e,t){if(D[e]=t,e in N)for(var n=N[e],i=0;i<n.length;i++)n[i]()},b.WP=function(e,t,n,i){var r;n in D?D[n](e,t,i,b):(r=function(){D[n](e,t,i,b)},n in N?N[n].push(r):N[n]=[r])},b.PG=function(e,t){for(var n=c,i=0;i<e.length;i++){var r=e[i];r in n||(n[r]={}),n=n[r]}n["@"]=t},function(e,t,n,i){if(t<e.length){if("number"in n){var r=n.number,o=parseFloat(e[t]);if(!isNaN(o))for(var a in r){if(i[a]=o,null!==(c=L(e,t+1,r[a],i)))return c;delete i[a]}}if("text"in n){r=n.text,o=e[t];for(a in r){if(i[a]=o,null!==(c=L(e,t+1,r[a],i)))return c;delete i[a]}}var c;if("fixed"in n)for(a in r=n.fixed)if(a==e[t])if(null!==(c=L(e,t+1,r[a],i)))return c}else if("@"in n)return n["@"]}),P=(b.goto=function(e,t){window.setTimeout(function(){t.startsWith("/")?b.run(document.body,t,!0):window.location.href=o(t)},10)},b.init=function(){b.run(document.body,C(window.location.pathname+window.location.hash),!1),window.onpopstate=function(){b.run(document.body,C(window.location.pathname+window.location.hash),!1)}},b.run=function(e,t,n){for(conKey in x)x[conKey].tree.nuke();var i=(t.startsWith("/")?t.substring(1):t).split("/"),r={session_id:"R"+Math.random()},i=L(i,0,c,r);h(e),null!=i?(i(e,i={service:k,data:null,view:(i=e,{tree:new AdamaTree,delta:{},parent:null,path:null,where:i}),current:"view"}),i.view.tree.subscribe(i.view.delta),i.view.tree.update(r),n&&window.history.pushState({},"",o(t))):"/404"!=t&&b.run(e,"/404"),e.appendChild(S)},{}),U=(b.SIGNOUT=function(){P={},localStorage.removeItem("identity_default");var e,t=[];for(e in x){var n=x[e];null!=n.ptr&&n.ptr.end({success:function(){},failure:function(){}}),t.push(e)}for(var i=0;i<t.length;i++)delete x[t[i]];b.goto(null,"/")},b.GOOGLE_SIGN_ON=function(e){k.InitConvertGoogleUser(e,{success:function(e){P.default=e.identity,localStorage.setItem("identity_default",e.identity),b.goto(null,"/")},failure:function(e){console.log("Google failure: "+e)}})},b.ID=function(e,t){!0===e&&(e="default");var n=null,i=function(){},r=localStorage.getItem("identity_"+e);if(r&&(P[e]=r),e.startsWith("direct:"))n=e.substr(7);else{if(!(e in P))return window.setTimeout(function(){b.goto(null,t)},10),{abort:!0};n=P[e],i=function(){delete P[e],localStorage.removeItem("identity_"+e)}}return{abort:!1,cleanup:i,identity:n}},{});b.PRCUDA=function(e,t){U[e]=t},b.CUDA=function(n,i,r,o,a,c){var u=p();o.__=s(10,function(){_(u),h(n);var e=!1,t=(r in U&&("function"==typeof(t=U[r])&&(e=t(o,i,a,b))),e=e||new AdamaTree,{service:i.service,data:{connection:i.connection,tree:e,delta:{},parent:null,path:null},view:d(i.view),current:"data"});c(t),m(t,u)})},b.ST=function(e){document.title=e.value,e.__=s(1,function(){document.title=e.value})},b.CONNECT=function(r,o,a){var c={view:function(){}};o.__=s(5,function(){var t,e,n,i;"key"in o&&"space"in o&&"name"in o&&(t=u(o.name),n=o.space+"/"+o.key,e=function(e){c.view=r.view.tree.subscribe(function(){null!=t.ptr&&t.ptr.update(r.view.tree.copy(),{success:function(){},failure:function(){}})}),e&&t.ptr.update(r.view.tree.copy(),{success:function(){},failure:function(){}})},null!=t.ptr&&t.bound==n?e(!0):(t.space=o.space,t.key=o.key,null!=t.ptr&&(t.ptr.end({success:function(){},failure:function(){}}),t.ptr=null),t.bound=n,(n=b.ID(o.identity,a)).abort||(i=n.identity,n.cleanup,c.view(),t.ptr=k.ConnectionCreate(i,o.space,o.key,r.view.tree.copy(),{next:function(e){t.set_connected(!0),"data"in e.delta&&t.tree.update(e.delta.data),"outstanding"in e.delta&&t.ondecide(e.delta.outstanding)},complete:function(){t.set_connected(!1)},should_retry:!0,retry_task_name:"Document Connection:"+o.space+"/"+o.key,failure:function(e){console.log("CONNECT FAILURE:"+o.space+"/"+o.key+" ["+o.name+"] "+e),t.set_connected(!1),t.ptr=null}}),t.tree.update({}),e(!1))))})},b.INTERNAL=function(e){return{service:e.service,data:{connection:null,tree:new AdamaTree,delta:{},parent:null,path:null},view:d(e.view),current:"data"}};return b.aUP=function(e,t,n,i,r){var n=b.ID(n,r);n.abort||(e.action="https://aws-us-east-2.adama-platform.com/~upload",e.method="post",e.enctype="multipart/form-data",(r=document.createElement("input")).type="hidden",r.name="identity",r.value=n.identity,e.appendChild(r),(n=document.createElement("iframe")).name="UPLOAD_"+Math.random(),n.width="1",n.height="1",e.appendChild(n),e.target=n.name)},b.aSO=function(t,n,i,e,r){var o=E(n,e);window.setTimeout(function(){a(t)},1),t.onsubmit=function(e){e.preventDefault();e=T(t);e.remember?localStorage.setItem("email_remember",e.email):localStorage.setItem("email_remember",""),k.AccountLogin(e.email,e.password,{success:function(e){o(!1),P[i]=e.identity,localStorage.setItem("identity_"+i,e.identity),b.goto(n.view,r)},failure:function(e){o(!0),console.log("Sign in failure:"+e)}})}},b.aSU=function(n,i,e,r){var o=E(i,e);n.onsubmit=function(e){e.preventDefault();var t=T(n);k.InitSetupAccount(t.email,{success:function(e){o(!1),localStorage.setItem("email",t.email),b.goto(i.view,r)},failure:function(e){o(!0),console.log("Sign up failure:")}})}},b.aSP=function(t,i,e,r){var o=E(i,e);t.onsubmit=function(e){e.preventDefault();var n=T(t);"email"in n||(n.email=localStorage.getItem("email")),k.InitCompleteAccount(n.email,!1,n.code,{success:function(e){var t=e.identity;k.AccountSetPassword(e.identity,n.password,{success:function(){o(!1),localStorage.setItem("identity_default",t),b.goto(i.view,r)},failure:function(e){o(!0)}})},failure:function(e){o(!0)}})}},b.aSD=function(n,i,r,e){var o=E(i,e);n.onsubmit=function(e){e.preventDefault();var t=performance.now();i.data.connection.ptr.send(r,T(n),{success:function(e){o(!1),console.log("Success|"+e.seq+";latency="+(performance.now()-t))},failure:function(e){o(!0),console.log("Send failure:"+e)}})}},window.rxhtml=b}();
