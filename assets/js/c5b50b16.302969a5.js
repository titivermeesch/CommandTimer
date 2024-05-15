"use strict";(self.webpackChunkdocs=self.webpackChunkdocs||[]).push([[27],{473:(e,n,o)=>{o.r(n),o.d(n,{assets:()=>c,contentTitle:()=>r,default:()=>h,frontMatter:()=>s,metadata:()=>d,toc:()=>a});var i=o(4848),t=o(8453);const s={sidebar_position:3},r="Conditions",d={id:"configuration/conditions",title:"Conditions",description:"DISCLAIMER: This is one of the most complex parts of CommandTimer. Please look at the rest of the documentation first before trying out the conditions engine",source:"@site/docs/configuration/conditions.md",sourceDirName:"configuration",slug:"/configuration/conditions",permalink:"/CommandTimer/docs/configuration/conditions",draft:!1,unlisted:!1,tags:[],version:"current",sidebarPosition:3,frontMatter:{sidebar_position:3},sidebar:"tutorialSidebar",previous:{title:"Schedules",permalink:"/CommandTimer/docs/configuration/schedules"},next:{title:"Others",permalink:"/CommandTimer/docs/configuration/others"}},c={},a=[{value:"Available conditions",id:"available-conditions",level:2},{value:"Condition values",id:"condition-values",level:2}];function l(e){const n={a:"a",code:"code",h1:"h1",h2:"h2",li:"li",p:"p",strong:"strong",ul:"ul",...(0,t.R)(),...e.components};return(0,i.jsxs)(i.Fragment,{children:[(0,i.jsx)(n.h1,{id:"conditions",children:"Conditions"}),"\n",(0,i.jsx)(n.p,{children:(0,i.jsx)(n.strong,{children:"DISCLAIMER: This is one of the most complex parts of CommandTimer. Please look at the rest of the documentation first before trying out the conditions engine"})}),"\n",(0,i.jsxs)(n.p,{children:["The conditions engine is used to add extra logic on top of your ",(0,i.jsx)(n.a,{href:"schedules#Fixed-times",children:"fixed times"})," and ",(0,i.jsx)(n.a,{href:"schedules#intervals",children:"intervals"}),"."]}),"\n",(0,i.jsx)(n.p,{children:"Conditions can have one of the following types:"}),"\n",(0,i.jsxs)(n.ul,{children:["\n",(0,i.jsxs)(n.li,{children:[(0,i.jsx)(n.code,{children:"SIMPLE"}),": This is the simplest form and defines one single condition. Result of this condition needs to be true (e.g. player IS operator)"]}),"\n",(0,i.jsxs)(n.li,{children:[(0,i.jsx)(n.code,{children:"NOT"}),": Same as above, but the result is reversed (e.g. player IS NOT operator)"]}),"\n",(0,i.jsxs)(n.li,{children:[(0,i.jsx)(n.code,{children:"AND"}),": Creates a group of conditions. Each of these conditions needs to be true to execute your ",(0,i.jsx)(n.a,{href:"../jargon#task",children:"task"}),"."]}),"\n",(0,i.jsxs)(n.li,{children:[(0,i.jsx)(n.code,{children:"OR"}),": Creates a group of conditions. One of these conditions needs to be true to execute your ",(0,i.jsx)(n.a,{href:"../jargon#task",children:"task"}),". Even if you have 50 conditions that do not match but one does, the ",(0,i.jsx)(n.a,{href:"../jargon#task",children:"task"})," will still go through"]}),"\n"]}),"\n",(0,i.jsxs)(n.p,{children:["When using ",(0,i.jsx)(n.code,{children:"AND"})," and ",(0,i.jsx)(n.code,{children:"OR"}),", more conditions can be nested. Nesting does not have a depth limit, meaning you could have an ",(0,i.jsx)(n.code,{children:"AND"})," condition in an ",(0,i.jsx)(n.code,{children:"OR"})," condition in an ",(0,i.jsx)(n.code,{children:"OR"})," condition in an ",(0,i.jsx)(n.code,{children:"AND"})," condition which itself contains 4 different ",(0,i.jsx)(n.code,{children:"SIMPLE"})," and ",(0,i.jsx)(n.code,{children:"NOT"})," conditions. It is good to draw a visual map yourself before doing these nested conditions, because it can be cumberstone to debug in case there is an issue."]}),"\n",(0,i.jsx)(n.h2,{id:"available-conditions",children:"Available conditions"}),"\n",(0,i.jsxs)(n.p,{children:["Conditions are made availabel through ",(0,i.jsx)(n.a,{href:"../extensions",children:"extensions"}),". This means that CommandTimer by default will not have any conditions, making the whole conditions engine useless if no ",(0,i.jsx)(n.a,{href:"../extensions",children:"extensions"})," are installed."]}),"\n",(0,i.jsx)(n.h2,{id:"condition-values",children:"Condition values"}),"\n",(0,i.jsx)(n.p,{children:"Certain conditions may require extra configuration values to work properly. These will be visible in the GUI"})]})}function h(e={}){const{wrapper:n}={...(0,t.R)(),...e.components};return n?(0,i.jsx)(n,{...e,children:(0,i.jsx)(l,{...e})}):l(e)}},8453:(e,n,o)=>{o.d(n,{R:()=>r,x:()=>d});var i=o(6540);const t={},s=i.createContext(t);function r(e){const n=i.useContext(s);return i.useMemo((function(){return"function"==typeof e?e(n):{...n,...e}}),[n,e])}function d(e){let n;return n=e.disableParentContext?"function"==typeof e.components?e.components(t):e.components||t:r(e.components),i.createElement(s.Provider,{value:n},e.children)}}}]);