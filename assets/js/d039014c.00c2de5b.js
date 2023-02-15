"use strict";(self.webpackChunkdocs=self.webpackChunkdocs||[]).push([[111],{3905:(e,t,r)=>{r.d(t,{Zo:()=>u,kt:()=>f});var n=r(7294);function a(e,t,r){return t in e?Object.defineProperty(e,t,{value:r,enumerable:!0,configurable:!0,writable:!0}):e[t]=r,e}function o(e,t){var r=Object.keys(e);if(Object.getOwnPropertySymbols){var n=Object.getOwnPropertySymbols(e);t&&(n=n.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),r.push.apply(r,n)}return r}function i(e){for(var t=1;t<arguments.length;t++){var r=null!=arguments[t]?arguments[t]:{};t%2?o(Object(r),!0).forEach((function(t){a(e,t,r[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(r)):o(Object(r)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(r,t))}))}return e}function s(e,t){if(null==e)return{};var r,n,a=function(e,t){if(null==e)return{};var r,n,a={},o=Object.keys(e);for(n=0;n<o.length;n++)r=o[n],t.indexOf(r)>=0||(a[r]=e[r]);return a}(e,t);if(Object.getOwnPropertySymbols){var o=Object.getOwnPropertySymbols(e);for(n=0;n<o.length;n++)r=o[n],t.indexOf(r)>=0||Object.prototype.propertyIsEnumerable.call(e,r)&&(a[r]=e[r])}return a}var l=n.createContext({}),c=function(e){var t=n.useContext(l),r=t;return e&&(r="function"==typeof e?e(t):i(i({},t),e)),r},u=function(e){var t=c(e.components);return n.createElement(l.Provider,{value:t},e.children)},p="mdxType",m={inlineCode:"code",wrapper:function(e){var t=e.children;return n.createElement(n.Fragment,{},t)}},d=n.forwardRef((function(e,t){var r=e.components,a=e.mdxType,o=e.originalType,l=e.parentName,u=s(e,["components","mdxType","originalType","parentName"]),p=c(r),d=a,f=p["".concat(l,".").concat(d)]||p[d]||m[d]||o;return r?n.createElement(f,i(i({ref:t},u),{},{components:r})):n.createElement(f,i({ref:t},u))}));function f(e,t){var r=arguments,a=t&&t.mdxType;if("string"==typeof e||a){var o=r.length,i=new Array(o);i[0]=d;var s={};for(var l in t)hasOwnProperty.call(t,l)&&(s[l]=t[l]);s.originalType=e,s[p]="string"==typeof e?e:a,i[1]=s;for(var c=2;c<o;c++)i[c]=r[c];return n.createElement.apply(null,i)}return n.createElement.apply(null,r)}d.displayName="MDXCreateElement"},7191:(e,t,r)=>{r.r(t),r.d(t,{assets:()=>l,contentTitle:()=>i,default:()=>m,frontMatter:()=>o,metadata:()=>s,toc:()=>c});var n=r(7462),a=(r(7294),r(3905));const o={},i="Miscellaneous",s={unversionedId:"miscellaneous",id:"miscellaneous",title:"Miscellaneous",description:"Metrics",source:"@site/docs/miscellaneous.md",sourceDirName:".",slug:"/miscellaneous",permalink:"/docs/miscellaneous",draft:!1,tags:[],version:"current",frontMatter:{},sidebar:"tutorialSidebar",previous:{title:"Jargon",permalink:"/docs/jargon"}},l={},c=[{value:"Metrics",id:"metrics",level:2},{value:"Auto updater",id:"auto-updater",level:2}],u={toc:c},p="wrapper";function m(e){let{components:t,...r}=e;return(0,a.kt)(p,(0,n.Z)({},u,r,{components:t,mdxType:"MDXLayout"}),(0,a.kt)("h1",{id:"miscellaneous"},"Miscellaneous"),(0,a.kt)("h2",{id:"metrics"},"Metrics"),(0,a.kt)("p",null,"Besides the common metrics, CommandTimer also registers the amount of loaded tasks and the amount of executions per\nsecond that are happening on your server. CommandTimer does not save any personal data that may be added in CommandTimer\nconfiguration files."),(0,a.kt)("p",null,(0,a.kt)("img",{parentName:"p",src:"https://bstats.org/signatures/bukkit/commandtimer.svg",alt:"stats"})),(0,a.kt)("p",null,"A detailed view of the statistics can be viewed ",(0,a.kt)("a",{parentName:"p",href:"https://bstats.org/plugin/bukkit/CommandTimer/9657"},"here")),(0,a.kt)("h2",{id:"auto-updater"},"Auto updater"),(0,a.kt)("p",null,"CommandTimer will not automatically update, but will show you a message in the chat that a new version can be\ndownloaded. It is always recommended to update to the latest version."),(0,a.kt)("p",null,"By default only OP players will view the update messages, but you can also give certain players the\npermission ",(0,a.kt)("inlineCode",{parentName:"p"},"commandtimer.update")," to show them the message."))}m.isMDXComponent=!0}}]);