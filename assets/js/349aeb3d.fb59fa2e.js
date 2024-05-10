"use strict";(self.webpackChunkdocs=self.webpackChunkdocs||[]).push([[648],{3905:(t,e,n)=>{n.d(e,{Zo:()=>p,kt:()=>s});var a=n(7294);function r(t,e,n){return e in t?Object.defineProperty(t,e,{value:n,enumerable:!0,configurable:!0,writable:!0}):t[e]=n,t}function i(t,e){var n=Object.keys(t);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(t);e&&(a=a.filter((function(e){return Object.getOwnPropertyDescriptor(t,e).enumerable}))),n.push.apply(n,a)}return n}function l(t){for(var e=1;e<arguments.length;e++){var n=null!=arguments[e]?arguments[e]:{};e%2?i(Object(n),!0).forEach((function(e){r(t,e,n[e])})):Object.getOwnPropertyDescriptors?Object.defineProperties(t,Object.getOwnPropertyDescriptors(n)):i(Object(n)).forEach((function(e){Object.defineProperty(t,e,Object.getOwnPropertyDescriptor(n,e))}))}return t}function d(t,e){if(null==t)return{};var n,a,r=function(t,e){if(null==t)return{};var n,a,r={},i=Object.keys(t);for(a=0;a<i.length;a++)n=i[a],e.indexOf(n)>=0||(r[n]=t[n]);return r}(t,e);if(Object.getOwnPropertySymbols){var i=Object.getOwnPropertySymbols(t);for(a=0;a<i.length;a++)n=i[a],e.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(t,n)&&(r[n]=t[n])}return r}var o=a.createContext({}),m=function(t){var e=a.useContext(o),n=e;return t&&(n="function"==typeof t?t(e):l(l({},e),t)),n},p=function(t){var e=m(t.components);return a.createElement(o.Provider,{value:e},t.children)},k="mdxType",N={inlineCode:"code",wrapper:function(t){var e=t.children;return a.createElement(a.Fragment,{},e)}},u=a.forwardRef((function(t,e){var n=t.components,r=t.mdxType,i=t.originalType,o=t.parentName,p=d(t,["components","mdxType","originalType","parentName"]),k=m(n),u=r,s=k["".concat(o,".").concat(u)]||k[u]||N[u]||i;return n?a.createElement(s,l(l({ref:e},p),{},{components:n})):a.createElement(s,l({ref:e},p))}));function s(t,e){var n=arguments,r=e&&e.mdxType;if("string"==typeof t||r){var i=n.length,l=new Array(i);l[0]=u;var d={};for(var o in e)hasOwnProperty.call(e,o)&&(d[o]=e[o]);d.originalType=t,d[k]="string"==typeof t?t:r,l[1]=d;for(var m=2;m<i;m++)l[m]=n[m];return a.createElement.apply(null,l)}return a.createElement.apply(null,n)}u.displayName="MDXCreateElement"},6786:(t,e,n)=>{n.r(e),n.d(e,{assets:()=>o,contentTitle:()=>l,default:()=>N,frontMatter:()=>i,metadata:()=>d,toc:()=>m});var a=n(7462),r=(n(7294),n(3905));const i={sidebar_position:6},l="JSON Schema",d={unversionedId:"jsonschema",id:"jsonschema",title:"JSON Schema",description:"If you prefer to use the JSON files directly instead of configuring tasks through the GUI, you can edit/create tasks",source:"@site/docs/jsonschema.md",sourceDirName:".",slug:"/jsonschema",permalink:"/CommandTimer/docs/jsonschema",draft:!1,tags:[],version:"current",sidebarPosition:6,frontMatter:{sidebar_position:6},sidebar:"tutorialSidebar",previous:{title:"Placeholders",permalink:"/CommandTimer/docs/placeholders"},next:{title:"Developers",permalink:"/CommandTimer/docs/developers/"}},o={},m=[{value:"Command",id:"command",level:2},{value:"Interval",id:"interval",level:2},{value:"Time",id:"time",level:2},{value:"Condition",id:"condition",level:2},{value:"Simple condition",id:"simple-condition",level:3},{value:"Condition parameter field",id:"condition-parameter-field",level:3},{value:"Event",id:"event",level:2},{value:"Event condition",id:"event-condition",level:3},{value:"Event simple condition",id:"event-simple-condition",level:3},{value:"Example",id:"example",level:2}],p={toc:m},k="wrapper";function N(t){let{components:e,...n}=t;return(0,r.kt)(k,(0,a.Z)({},p,n,{components:e,mdxType:"MDXLayout"}),(0,r.kt)("h1",{id:"json-schema"},"JSON Schema"),(0,r.kt)("p",null,"If you prefer to use the JSON files directly instead of configuring tasks through the GUI, you can edit/create tasks\nfollowing this JSON schema"),(0,r.kt)("table",null,(0,r.kt)("thead",{parentName:"table"},(0,r.kt)("tr",{parentName:"thead"},(0,r.kt)("th",{parentName:"tr",align:null},"Field"),(0,r.kt)("th",{parentName:"tr",align:null},"Description"),(0,r.kt)("th",{parentName:"tr",align:null},"Type"))),(0,r.kt)("tbody",{parentName:"table"},(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"name")),(0,r.kt)("td",{parentName:"tr",align:null},"Name of the task. Should only contain alphanumerical characters or underscores"),(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"string"))),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"commands")),(0,r.kt)("td",{parentName:"tr",align:null},"List of commands to execute"),(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("a",{parentName:"td",href:"#command"},(0,r.kt)("inlineCode",{parentName:"a"},"Commands[]")))),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"interval")),(0,r.kt)("td",{parentName:"tr",align:null},"Time between each command execution"),(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("a",{parentName:"td",href:"#interval"},(0,r.kt)("inlineCode",{parentName:"a"},"Interval")))),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"times")),(0,r.kt)("td",{parentName:"tr",align:null},"List of fixed times to execute the ",(0,r.kt)("a",{parentName:"td",href:"jargon#task"},"task")," on"),(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("a",{parentName:"td",href:"#time"},(0,r.kt)("inlineCode",{parentName:"a"},"Time[]")))),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"random")),(0,r.kt)("td",{parentName:"tr",align:null},"Defines a value to calculate the execution chance. ",(0,r.kt)("inlineCode",{parentName:"td"},"1")," is ",(0,r.kt)("inlineCode",{parentName:"td"},"100%"),", ",(0,r.kt)("inlineCode",{parentName:"td"},"0.5")," is ",(0,r.kt)("inlineCode",{parentName:"td"},"50%"),", ",(0,r.kt)("inlineCode",{parentName:"td"},"0")," is ",(0,r.kt)("inlineCode",{parentName:"td"},"0%")," and so on"),(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"number"))),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"days")),(0,r.kt)("td",{parentName:"tr",align:null},"List of days on which the ",(0,r.kt)("a",{parentName:"td",href:"jargon#task"},"task")," can be executed"),(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"MONDAY"),", ",(0,r.kt)("inlineCode",{parentName:"td"},"TUESDAY"),", ",(0,r.kt)("inlineCode",{parentName:"td"},"WEDNESDAY"),", ",(0,r.kt)("inlineCode",{parentName:"td"},"THURSDAY"),", ",(0,r.kt)("inlineCode",{parentName:"td"},"FRIDAY"),", ",(0,r.kt)("inlineCode",{parentName:"td"},"SATURDAY"),", ",(0,r.kt)("inlineCode",{parentName:"td"},"SUNDAY"))),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"executionLimit")),(0,r.kt)("td",{parentName:"tr",align:null},"Maximum amount of executions. Set to ",(0,r.kt)("inlineCode",{parentName:"td"},"-1")," to disable feature"),(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"number"))),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"timesExecuted")),(0,r.kt)("td",{parentName:"tr",align:null},"Amount of times the ",(0,r.kt)("a",{parentName:"td",href:"jargon#task"},"task")," has been executed. ",(0,r.kt)("strong",{parentName:"td"},"Do not update manually")),(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"number"))),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"lastExecutedCommandIndex")),(0,r.kt)("td",{parentName:"tr",align:null},"Index of the last executed command. This is used in case the execution mode is set to ",(0,r.kt)("inlineCode",{parentName:"td"},"INTERVAL"),". ",(0,r.kt)("strong",{parentName:"td"},"Do not update manually")),(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"number"))),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"lastExecuted")),(0,r.kt)("td",{parentName:"tr",align:null},"Date of the latest ",(0,r.kt)("a",{parentName:"td",href:"jargon#task"},"task")," execution. ",(0,r.kt)("strong",{parentName:"td"},"Do not update manually")),(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"Date"))),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"commandExecutionMode")),(0,r.kt)("td",{parentName:"tr",align:null},"Execution mode for the commands"),(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("a",{parentName:"td",href:"configuration/commands#execution-modes"},(0,r.kt)("inlineCode",{parentName:"a"},"Execution Mode")))),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"commandExecutionInterval")),(0,r.kt)("td",{parentName:"tr",align:null},"Defines time between command executions when using ",(0,r.kt)("inlineCode",{parentName:"td"},"INTERVAL")," ",(0,r.kt)("a",{parentName:"td",href:"configuration/commands#execution-modes"},"execution mode")),(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("a",{parentName:"td",href:"#interval"},(0,r.kt)("inlineCode",{parentName:"a"},"Interval")))),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"active")),(0,r.kt)("td",{parentName:"tr",align:null},"Defines if the ",(0,r.kt)("a",{parentName:"td",href:"jargon#task"},"task")," is active or not"),(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"boolean"))),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"resetExecutionsAfterRestart")),(0,r.kt)("td",{parentName:"tr",align:null},"Defines if the value ",(0,r.kt)("inlineCode",{parentName:"td"},"executionLimit")," needs to be reset when plugin restarts"),(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"boolean"))),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"condition")),(0,r.kt)("td",{parentName:"tr",align:null},"Configuration for the ",(0,r.kt)("a",{parentName:"td",href:"configuration/conditions"},"Conditions Engine")),(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("a",{parentName:"td",href:"#condition"},(0,r.kt)("inlineCode",{parentName:"a"},"Condition")))),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"event")),(0,r.kt)("td",{parentName:"tr",align:null},"Configuration for the ",(0,r.kt)("a",{parentName:"td",href:"events"},"Events Engine")),(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("a",{parentName:"td",href:"#event"},(0,r.kt)("inlineCode",{parentName:"a"},"Event[]")))))),(0,r.kt)("h2",{id:"command"},"Command"),(0,r.kt)("table",null,(0,r.kt)("thead",{parentName:"table"},(0,r.kt)("tr",{parentName:"thead"},(0,r.kt)("th",{parentName:"tr",align:null},"Field"),(0,r.kt)("th",{parentName:"tr",align:null},"Description"),(0,r.kt)("th",{parentName:"tr",align:null},"Type"))),(0,r.kt)("tbody",{parentName:"table"},(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"command")),(0,r.kt)("td",{parentName:"tr",align:null},"Command to execute. Do not include ",(0,r.kt)("inlineCode",{parentName:"td"},"/")," in front of the command"),(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"string"))),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"gender")),(0,r.kt)("td",{parentName:"tr",align:null},"Gender of the command"),(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("a",{parentName:"td",href:"configuration/commands#genders"},"Gender"))),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"interval")),(0,r.kt)("td",{parentName:"tr",align:null},"Defined the iteration interval between each command when the execution iterates over all users"),(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("a",{parentName:"td",href:"#interval"},"Interval"))))),(0,r.kt)("h2",{id:"interval"},"Interval"),(0,r.kt)("table",null,(0,r.kt)("thead",{parentName:"table"},(0,r.kt)("tr",{parentName:"thead"},(0,r.kt)("th",{parentName:"tr",align:null},"Field"),(0,r.kt)("th",{parentName:"tr",align:null},"Description"),(0,r.kt)("th",{parentName:"tr",align:null},"Type"))),(0,r.kt)("tbody",{parentName:"table"},(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"days")),(0,r.kt)("td",{parentName:"tr",align:null},"Amount of days"),(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"number"))),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"hours")),(0,r.kt)("td",{parentName:"tr",align:null},"Amount of hours"),(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"number"))),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"minutes")),(0,r.kt)("td",{parentName:"tr",align:null},"Amount of minutes"),(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"number"))),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"seconds")),(0,r.kt)("td",{parentName:"tr",align:null},"Amount of seconds"),(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"number"))))),(0,r.kt)("h2",{id:"time"},"Time"),(0,r.kt)("table",null,(0,r.kt)("thead",{parentName:"table"},(0,r.kt)("tr",{parentName:"thead"},(0,r.kt)("th",{parentName:"tr",align:null},"Field"),(0,r.kt)("th",{parentName:"tr",align:null},"Description"),(0,r.kt)("th",{parentName:"tr",align:null},"Type"))),(0,r.kt)("tbody",{parentName:"table"},(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"time1")),(0,r.kt)("td",{parentName:"tr",align:null},"Value of the ",(0,r.kt)("a",{parentName:"td",href:"configuration/schedules#normal-fixed-times"},"fixed time")," or start of ",(0,r.kt)("a",{parentName:"td",href:"configuration/schedules#ranged-fixed-times"},"range")," if ",(0,r.kt)("inlineCode",{parentName:"td"},"time2")," is filled"),(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"string")," in ",(0,r.kt)("inlineCode",{parentName:"td"},"HH:mm:ss")," format")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"time2")),(0,r.kt)("td",{parentName:"tr",align:null},"Value of the end time for a ",(0,r.kt)("a",{parentName:"td",href:"configuration/schedules#ranged-fixed-times"},"range")),(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"string")," in ",(0,r.kt)("inlineCode",{parentName:"td"},"HH:mm:ss")," format")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"isMinecraftTime")),(0,r.kt)("td",{parentName:"tr",align:null},"Defines if the filled in time is Minecraft time or normal time"),(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"boolean"))))),(0,r.kt)("h2",{id:"condition"},"Condition"),(0,r.kt)("p",null,"More information about all these settings can be found ",(0,r.kt)("a",{parentName:"p",href:"configuration/conditions"},"here"),"."),(0,r.kt)("p",null,"As you can see, the condition object has a recursive field called ",(0,r.kt)("inlineCode",{parentName:"p"},"conditions"),". This is because conditions can be nested\nmultiple times to handle complex use-cases."),(0,r.kt)("table",null,(0,r.kt)("thead",{parentName:"table"},(0,r.kt)("tr",{parentName:"thead"},(0,r.kt)("th",{parentName:"tr",align:null},"Field"),(0,r.kt)("th",{parentName:"tr",align:null},"Description"),(0,r.kt)("th",{parentName:"tr",align:null},"Type"))),(0,r.kt)("tbody",{parentName:"table"},(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"conditionType")),(0,r.kt)("td",{parentName:"tr",align:null},"Type of the condition"),(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"SIMPLE"),", ",(0,r.kt)("inlineCode",{parentName:"td"},"NOT"),", ",(0,r.kt)("inlineCode",{parentName:"td"},"AND"),", ",(0,r.kt)("inlineCode",{parentName:"td"},"OR"))),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"simpleCondition")),(0,r.kt)("td",{parentName:"tr",align:null},"A condition configuration in its most simple form. Is only taken into account if ",(0,r.kt)("inlineCode",{parentName:"td"},"conditionType")," is ",(0,r.kt)("inlineCode",{parentName:"td"},"SIMPLE")," or ",(0,r.kt)("inlineCode",{parentName:"td"},"NOT")),(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("a",{parentName:"td",href:"#simple-condition"},(0,r.kt)("inlineCode",{parentName:"a"},"SimpleCondition")))),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"conditions")),(0,r.kt)("td",{parentName:"tr",align:null},"List of conditions. Is only taken into account if ",(0,r.kt)("inlineCode",{parentName:"td"},"conditionType")," is ",(0,r.kt)("inlineCode",{parentName:"td"},"AND")," or ",(0,r.kt)("inlineCode",{parentName:"td"},"OR")),(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("a",{parentName:"td",href:"#condition"},(0,r.kt)("inlineCode",{parentName:"a"},"Condition[]")))))),(0,r.kt)("h3",{id:"simple-condition"},"Simple condition"),(0,r.kt)("table",null,(0,r.kt)("thead",{parentName:"table"},(0,r.kt)("tr",{parentName:"thead"},(0,r.kt)("th",{parentName:"tr",align:null},"Field"),(0,r.kt)("th",{parentName:"tr",align:null},"Description"),(0,r.kt)("th",{parentName:"tr",align:null},"Type"))),(0,r.kt)("tbody",{parentName:"table"},(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"conditionGroup")),(0,r.kt)("td",{parentName:"tr",align:null},"Name of the extension this condition belongs to"),(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"string"))),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"rule")),(0,r.kt)("td",{parentName:"tr",align:null},"Specific condition rule contained in the selected ",(0,r.kt)("inlineCode",{parentName:"td"},"conditionGroup")),(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"string"))),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"conditionParamFields")),(0,r.kt)("td",{parentName:"tr",align:null},"Extra values required by the selected ",(0,r.kt)("inlineCode",{parentName:"td"},"rule")),(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("a",{parentName:"td",href:"#condition-parameter-field"},(0,r.kt)("inlineCode",{parentName:"a"},"ConditionParamterField[]")))))),(0,r.kt)("h3",{id:"condition-parameter-field"},"Condition parameter field"),(0,r.kt)("table",null,(0,r.kt)("thead",{parentName:"table"},(0,r.kt)("tr",{parentName:"thead"},(0,r.kt)("th",{parentName:"tr",align:null},"Field"),(0,r.kt)("th",{parentName:"tr",align:null},"Description"),(0,r.kt)("th",{parentName:"tr",align:null},"Type"))),(0,r.kt)("tbody",{parentName:"table"},(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"name")),(0,r.kt)("td",{parentName:"tr",align:null},"Name of the parameter"),(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"string"))),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"value")),(0,r.kt)("td",{parentName:"tr",align:null},"Given value for the configured parameter"),(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"any"))))),(0,r.kt)("h2",{id:"event"},"Event"),(0,r.kt)("p",null,"Documentation about the Events Engine can be found ",(0,r.kt)("a",{parentName:"p",href:"events"},"here")),(0,r.kt)("table",null,(0,r.kt)("thead",{parentName:"table"},(0,r.kt)("tr",{parentName:"thead"},(0,r.kt)("th",{parentName:"tr",align:null},"Field"),(0,r.kt)("th",{parentName:"tr",align:null},"Description"),(0,r.kt)("th",{parentName:"tr",align:null},"Type"))),(0,r.kt)("tbody",{parentName:"table"},(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"active")),(0,r.kt)("td",{parentName:"tr",align:null},"Defines if specific event is enabled"),(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"boolean"))),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"conditionGroup")),(0,r.kt)("td",{parentName:"tr",align:null},"Name of the extension this event belongs to"),(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"string"))),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"event")),(0,r.kt)("td",{parentName:"tr",align:null},"Specific event name contained in the selected ",(0,r.kt)("inlineCode",{parentName:"td"},"conditionGroup")),(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"string"))),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"condition")),(0,r.kt)("td",{parentName:"tr",align:null},"Configured condition for this event to execute. This is not the same as normal conditions"),(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("a",{parentName:"td",href:"#event-condition"},(0,r.kt)("inlineCode",{parentName:"a"},"EventCondition")))))),(0,r.kt)("h3",{id:"event-condition"},"Event condition"),(0,r.kt)("table",null,(0,r.kt)("thead",{parentName:"table"},(0,r.kt)("tr",{parentName:"thead"},(0,r.kt)("th",{parentName:"tr",align:null},"Field"),(0,r.kt)("th",{parentName:"tr",align:null},"Description"),(0,r.kt)("th",{parentName:"tr",align:null},"Type"))),(0,r.kt)("tbody",{parentName:"table"},(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"conditionType")),(0,r.kt)("td",{parentName:"tr",align:null},"Type of the condition"),(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"SIMPLE"),", ",(0,r.kt)("inlineCode",{parentName:"td"},"NOT"),", ",(0,r.kt)("inlineCode",{parentName:"td"},"AND"),", ",(0,r.kt)("inlineCode",{parentName:"td"},"OR"))),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"simpleCondition")),(0,r.kt)("td",{parentName:"tr",align:null},"A condition configuration in its most simple form. Is only taken into account if ",(0,r.kt)("inlineCode",{parentName:"td"},"conditionType")," is ",(0,r.kt)("inlineCode",{parentName:"td"},"SIMPLE")," or ",(0,r.kt)("inlineCode",{parentName:"td"},"NOT")),(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("a",{parentName:"td",href:"#event-simple-condition"},(0,r.kt)("inlineCode",{parentName:"a"},"EventSimpleCondition")))),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"conditions")),(0,r.kt)("td",{parentName:"tr",align:null},"List of conditions. Is only taken into account if ",(0,r.kt)("inlineCode",{parentName:"td"},"conditionType")," is ",(0,r.kt)("inlineCode",{parentName:"td"},"AND")," or ",(0,r.kt)("inlineCode",{parentName:"td"},"OR")),(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("a",{parentName:"td",href:"#event-condition"},(0,r.kt)("inlineCode",{parentName:"a"},"Condition[]")))))),(0,r.kt)("h3",{id:"event-simple-condition"},"Event simple condition"),(0,r.kt)("table",null,(0,r.kt)("thead",{parentName:"table"},(0,r.kt)("tr",{parentName:"thead"},(0,r.kt)("th",{parentName:"tr",align:null},"Field"),(0,r.kt)("th",{parentName:"tr",align:null},"Description"),(0,r.kt)("th",{parentName:"tr",align:null},"Type"))),(0,r.kt)("tbody",{parentName:"table"},(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"fieldName")),(0,r.kt)("td",{parentName:"tr",align:null},"Name of the parameter"),(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"string"))),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"value")),(0,r.kt)("td",{parentName:"tr",align:null},"Given value for the configured parameter"),(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"any"))),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"compare")),(0,r.kt)("td",{parentName:"tr",align:null},"Compare value between event value and passed ",(0,r.kt)("inlineCode",{parentName:"td"},"value")),(0,r.kt)("td",{parentName:"tr",align:null},(0,r.kt)("inlineCode",{parentName:"td"},"EQUAL"),", ",(0,r.kt)("inlineCode",{parentName:"td"},"GREATER_THAN"),", ",(0,r.kt)("inlineCode",{parentName:"td"},"LESS_THAN"),", ",(0,r.kt)("inlineCode",{parentName:"td"},"GREATER_OR_EQUAL_THAN"),", ",(0,r.kt)("inlineCode",{parentName:"td"},"LESS_OR_EQUAL_THEN"))))),(0,r.kt)("h2",{id:"example"},"Example"),(0,r.kt)("details",null,(0,r.kt)("summary",null,"Complete example"),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-json"},'{\n  "name": "alert_job_levelup",\n  "commands": [\n    {\n      "command": "say test",\n      "gender": "CONSOLE"\n    }\n  ],\n  "interval": {\n    "days": 1,\n    "hours": 0,\n    "minutes": 0,\n    "seconds": 5\n  },\n  "times": [\n    {\n      "time1": "14:00:00",\n      "time2": "14:00:00",\n      "isMinecraftTime": false\n    }\n  ],\n  "random": 1.0,\n  "days": [\n    "MONDAY",\n    "TUESDAY",\n    "WEDNESDAY",\n    "THURSDAY",\n    "FRIDAY",\n    "SATURDAY",\n    "SUNDAY"\n  ],\n  "executionLimit": -1,\n  "timesExecuted": 7,\n  "lastExecutedCommandIndex": 0,\n  "lastExecuted": "Feb 8, 2023, 9:19:51 PM",\n  "commandExecutionMode": "INTERVAL",\n  "commandExecutionInterval": {\n    "days": 0,\n    "hours": 0,\n    "minutes": 0,\n    "seconds": 1\n  },\n  "active": true,\n  "resetExecutionsAfterRestart": false,\n  "condition": {\n    "conditionType": "SIMPLE",\n    "conditions": [],\n    "simpleCondition": {\n      "conditionGroup": "JOBSREBORN",\n      "rule": "HAS_SPECIFIC_JOB",\n      "conditionParamFields": [\n        {\n          "name": "required_job",\n          "value": ""\n        },\n        {\n          "name": "LEVEL",\n          "value": 0\n        }\n      ]\n    }\n  },\n  "events": [\n    {\n      "active": true,\n      "conditionGroup": "JOBSREBORN",\n      "event": "LEVEL_UP",\n      "condition": {\n        "conditionType": "SIMPLE",\n        "conditions": [],\n        "simpleCondition": {\n          "fieldName": "LEVEL",\n          "value": 12.0,\n          "compare": "EQUAL"\n        }\n      }\n    }\n  ]\n}\n'))))}N.isMDXComponent=!0}}]);