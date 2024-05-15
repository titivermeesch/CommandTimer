"use strict";(self.webpackChunkdocs=self.webpackChunkdocs||[]).push([[679],{3905:(e,t,r)=>{r.d(t,{Zo:()=>u,kt:()=>d});var n=r(7294);function o(e,t,r){return t in e?Object.defineProperty(e,t,{value:r,enumerable:!0,configurable:!0,writable:!0}):e[t]=r,e}function i(e,t){var r=Object.keys(e);if(Object.getOwnPropertySymbols){var n=Object.getOwnPropertySymbols(e);t&&(n=n.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),r.push.apply(r,n)}return r}function a(e){for(var t=1;t<arguments.length;t++){var r=null!=arguments[t]?arguments[t]:{};t%2?i(Object(r),!0).forEach((function(t){o(e,t,r[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(r)):i(Object(r)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(r,t))}))}return e}function s(e,t){if(null==e)return{};var r,n,o=function(e,t){if(null==e)return{};var r,n,o={},i=Object.keys(e);for(n=0;n<i.length;n++)r=i[n],t.indexOf(r)>=0||(o[r]=e[r]);return o}(e,t);if(Object.getOwnPropertySymbols){var i=Object.getOwnPropertySymbols(e);for(n=0;n<i.length;n++)r=i[n],t.indexOf(r)>=0||Object.prototype.propertyIsEnumerable.call(e,r)&&(o[r]=e[r])}return o}var c=n.createContext({}),l=function(e){var t=n.useContext(c),r=t;return e&&(r="function"==typeof e?e(t):a(a({},t),e)),r},u=function(e){var t=l(e.components);return n.createElement(c.Provider,{value:t},e.children)},p="mdxType",f={inlineCode:"code",wrapper:function(e){var t=e.children;return n.createElement(n.Fragment,{},t)}},m=n.forwardRef((function(e,t){var r=e.components,o=e.mdxType,i=e.originalType,c=e.parentName,u=s(e,["components","mdxType","originalType","parentName"]),p=l(r),m=o,d=p["".concat(c,".").concat(m)]||p[m]||f[m]||i;return r?n.createElement(d,a(a({ref:t},u),{},{components:r})):n.createElement(d,a({ref:t},u))}));function d(e,t){var r=arguments,o=t&&t.mdxType;if("string"==typeof e||o){var i=r.length,a=new Array(i);a[0]=m;var s={};for(var c in t)hasOwnProperty.call(t,c)&&(s[c]=t[c]);s.originalType=e,s[p]="string"==typeof e?e:o,a[1]=s;for(var l=2;l<i;l++)a[l]=r[l];return n.createElement.apply(null,a)}return n.createElement.apply(null,r)}m.displayName="MDXCreateElement"},9721:(e,t,r)=>{r.r(t),r.d(t,{assets:()=>c,contentTitle:()=>a,default:()=>f,frontMatter:()=>i,metadata:()=>s,toc:()=>l});var n=r(7462),o=(r(7294),r(3905));const i={sidebar_position:4},a="Others",s={unversionedId:"configuration/others",id:"configuration/others",title:"Others",description:"General limits",source:"@site/docs/configuration/others.md",sourceDirName:"configuration",slug:"/configuration/others",permalink:"/CommandTimer/docs/configuration/others",draft:!1,tags:[],version:"current",sidebarPosition:4,frontMatter:{sidebar_position:4},sidebar:"tutorialSidebar",previous:{title:"Conditions",permalink:"/CommandTimer/docs/configuration/conditions"},next:{title:"Extensions",permalink:"/CommandTimer/docs/extensions/"}},c={},l=[{value:"General limits",id:"general-limits",level:2},{value:"Execution limits",id:"execution-limits",level:3}],u={toc:l},p="wrapper";function f(e){let{components:t,...r}=e;return(0,o.kt)(p,(0,n.Z)({},u,r,{components:t,mdxType:"MDXLayout"}),(0,o.kt)("h1",{id:"others"},"Others"),(0,o.kt)("h2",{id:"general-limits"},"General limits"),(0,o.kt)("h3",{id:"execution-limits"},"Execution limits"),(0,o.kt)("p",null,"By default, a ",(0,o.kt)("a",{parentName:"p",href:"../jargon#task"},"task")," can be executed an infinite amount of times. If you want the ",(0,o.kt)("a",{parentName:"p",href:"../jargon#task"},"task")," to only be executed e.g. a 100 times, you can set your execution limit to 100."),(0,o.kt)("p",null,"If you want to set the execution limit to infinite again, use the value -1."),(0,o.kt)("p",null,"CommandTimer will keep track of the executions between server reloads or restarts, but this can also be disabled by activating the reset execution after restart feature."),(0,o.kt)("p",null,"This is very useful if you only want to execute a command on server start."))}f.isMDXComponent=!0}}]);