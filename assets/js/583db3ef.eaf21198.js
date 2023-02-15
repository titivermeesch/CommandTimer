"use strict";(self.webpackChunkdocs=self.webpackChunkdocs||[]).push([[363],{3905:(e,t,n)=>{n.d(t,{Zo:()=>m,kt:()=>h});var i=n(7294);function o(e,t,n){return t in e?Object.defineProperty(e,t,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[t]=n,e}function a(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var i=Object.getOwnPropertySymbols(e);t&&(i=i.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,i)}return n}function r(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?a(Object(n),!0).forEach((function(t){o(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):a(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}function s(e,t){if(null==e)return{};var n,i,o=function(e,t){if(null==e)return{};var n,i,o={},a=Object.keys(e);for(i=0;i<a.length;i++)n=a[i],t.indexOf(n)>=0||(o[n]=e[n]);return o}(e,t);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(e);for(i=0;i<a.length;i++)n=a[i],t.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(e,n)&&(o[n]=e[n])}return o}var l=i.createContext({}),d=function(e){var t=i.useContext(l),n=t;return e&&(n="function"==typeof e?e(t):r(r({},t),e)),n},m=function(e){var t=d(e.components);return i.createElement(l.Provider,{value:t},e.children)},c="mdxType",p={inlineCode:"code",wrapper:function(e){var t=e.children;return i.createElement(i.Fragment,{},t)}},u=i.forwardRef((function(e,t){var n=e.components,o=e.mdxType,a=e.originalType,l=e.parentName,m=s(e,["components","mdxType","originalType","parentName"]),c=d(n),u=o,h=c["".concat(l,".").concat(u)]||c[u]||p[u]||a;return n?i.createElement(h,r(r({ref:t},m),{},{components:n})):i.createElement(h,r({ref:t},m))}));function h(e,t){var n=arguments,o=t&&t.mdxType;if("string"==typeof e||o){var a=n.length,r=new Array(a);r[0]=u;var s={};for(var l in t)hasOwnProperty.call(t,l)&&(s[l]=t[l]);s.originalType=e,s[c]="string"==typeof e?e:o,r[1]=s;for(var d=2;d<a;d++)r[d]=n[d];return i.createElement.apply(null,r)}return i.createElement.apply(null,n)}u.displayName="MDXCreateElement"},5073:(e,t,n)=>{n.r(t),n.d(t,{assets:()=>l,contentTitle:()=>r,default:()=>p,frontMatter:()=>a,metadata:()=>s,toc:()=>d});var i=n(7462),o=(n(7294),n(3905));const a={sidebar_position:3},r="Extensions",s={unversionedId:"extensions/index",id:"extensions/index",title:"Extensions",description:"Extensions are used to add extra functionality that is not there by default. Each extension comes in the form of a jar",source:"@site/docs/extensions/index.md",sourceDirName:"extensions",slug:"/extensions/",permalink:"/docs/extensions/",draft:!1,tags:[],version:"current",sidebarPosition:3,frontMatter:{sidebar_position:3},sidebar:"tutorialSidebar",previous:{title:"Others",permalink:"/docs/configuration/others"},next:{title:"Events",permalink:"/docs/events/"}},l={},d=[{value:"Installing extensions",id:"installing-extensions",level:2},{value:"Available extensions",id:"available-extensions",level:2},{value:"Oficial extensions",id:"oficial-extensions",level:3},{value:"Community extensions",id:"community-extensions",level:3},{value:"Creating your own extension",id:"creating-your-own-extension",level:2},{value:"Adding extension conditions",id:"adding-extension-conditions",level:3},{value:"Examples",id:"examples",level:3}],m={toc:d},c="wrapper";function p(e){let{components:t,...n}=e;return(0,o.kt)(c,(0,i.Z)({},m,n,{components:t,mdxType:"MDXLayout"}),(0,o.kt)("h1",{id:"extensions"},"Extensions"),(0,o.kt)("p",null,"Extensions are used to add extra functionality that is not there by default. Each extension comes in the form of a jar\nfile that CommandTimer will use to extend its original functionality."),(0,o.kt)("h2",{id:"installing-extensions"},"Installing extensions"),(0,o.kt)("p",null,"Extensions should not be treated as plugins. Extensions need to be placed under the ",(0,o.kt)("inlineCode",{parentName:"p"},"commandtimer/extensions")," folder\ninstead."),(0,o.kt)("p",null,"Once the jar file is in that folder, you should see the extension in different menus (for example the conditions menu)"),(0,o.kt)("h2",{id:"available-extensions"},"Available extensions"),(0,o.kt)("p",null,"Here is a list of extensions that can be installed. Official extensions are made by the team behind CommandTimer, while\ncommunity extensions, as the name says, are made by the community."),(0,o.kt)("h3",{id:"oficial-extensions"},"Oficial extensions"),(0,o.kt)("ul",null,(0,o.kt)("li",{parentName:"ul"},(0,o.kt)("a",{parentName:"li",href:"https://www.spigotmc.org/resources/time-conditions-commandtimer-extension.105591/"},"Time Extension")),(0,o.kt)("li",{parentName:"ul"},(0,o.kt)("a",{parentName:"li",href:"https://www.spigotmc.org/resources/player-conditions-commandtimer-extension.97186/"},"Player Extension")),(0,o.kt)("li",{parentName:"ul"},(0,o.kt)("a",{parentName:"li",href:"https://www.spigotmc.org/resources/server-conditions-commandtimer-extension.97188/"},"Server Extension")),(0,o.kt)("li",{parentName:"ul"},(0,o.kt)("a",{parentName:"li",href:"https://www.spigotmc.org/resources/griefdefender-conditions-commandtimer-extension.106330/"},"GriefDefender Extension"))),(0,o.kt)("h3",{id:"community-extensions"},"Community extensions"),(0,o.kt)("p",null,"You made an extension and want to add it here? Open a ticket on ",(0,o.kt)("a",{parentName:"p",href:"https://github.com/titivermeesch/CommandTimer"},"GitHub")),(0,o.kt)("h2",{id:"creating-your-own-extension"},"Creating your own extension"),(0,o.kt)("p",null,"Creating an extension is pretty straight forward. Follow this guide to get started."),(0,o.kt)("ol",null,(0,o.kt)("li",{parentName:"ol"},"Create a new Java project. Ideally you use a dependency manager like Maven or Gradle"),(0,o.kt)("li",{parentName:"ol"},"Add CommandTimer as dependency for your project. See ",(0,o.kt)("a",{parentName:"li",href:"/docs/developers/"},"Developers Documentation")," for further details"),(0,o.kt)("li",{parentName:"ol"},"Create a class which will be the entry point for your extension. This class ",(0,o.kt)("strong",{parentName:"li"},"needs")," to\nextend ",(0,o.kt)("inlineCode",{parentName:"li"},"ConditionExtension")," (located at ",(0,o.kt)("inlineCode",{parentName:"li"},"me.playbosswar.com.api.ConditionExtension"),")"),(0,o.kt)("li",{parentName:"ol"},"Once that main class is set up, you can now continue reading to add true functionality. I would recommend to first\ncompile your jar file and check if it appears in the condition menu in-game.")),(0,o.kt)("h3",{id:"adding-extension-conditions"},"Adding extension conditions"),(0,o.kt)("p",null,"One of the possibilities of extensions is adding conditions for the conditions engine."),(0,o.kt)("p",null,"A condition is represented by a class extending ",(0,o.kt)("inlineCode",{parentName:"p"},"ConditionRule")," (located at ",(0,o.kt)("inlineCode",{parentName:"p"},"me.playbosswar.com.api.ConditionRule"),"). You\nwill also need to add the ",(0,o.kt)("a",{parentName:"p",href:"https://github.com/j-easy/easy-rules"},"Easy Rules")," package to your project. You don't need to\nshade this package though, CommandTimer already comes pre-packed with it. The version used by CommandTimer is ",(0,o.kt)("inlineCode",{parentName:"p"},"4.1.0")," so\nideally your extension also uses the same version."),(0,o.kt)("p",null,"The most important part of a condition is the ",(0,o.kt)("inlineCode",{parentName:"p"},"ConditionRule#evaluate(Facts facts)")," method. This one will be triggered\nfor every CommandTimer execution loop (around once a second) and check if the condition is met."),(0,o.kt)("p",null,"In these facts, only ",(0,o.kt)("inlineCode",{parentName:"p"},"player")," is available by default. ",(0,o.kt)("strong",{parentName:"p"},"Player will not be available depending on certain genders like\nCONSOLE"),"."),(0,o.kt)("p",null,"To access the player that is currently being used to check the condition, one could do:"),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-java"},'Player p = facts.get("player");\n')),(0,o.kt)("p",null,"Another important method here is ",(0,o.kt)("inlineCode",{parentName:"p"},"ConditionRule#getNeededValues()"),". This method should return a list of ",(0,o.kt)("inlineCode",{parentName:"p"},"NeededValue"),"\nspecifying which external values the condition needs. You can see this as extra configuration values provided by the\nplayer. Here is a list of supported types:"),(0,o.kt)("ul",null,(0,o.kt)("li",{parentName:"ul"},"Double"),(0,o.kt)("li",{parentName:"ul"},"Integer"),(0,o.kt)("li",{parentName:"ul"},"String"),(0,o.kt)("li",{parentName:"ul"},"ConditionCompare")),(0,o.kt)("p",null,"All these types are the native Java types, except ",(0,o.kt)("inlineCode",{parentName:"p"},"ConditionCompare"),", which is used to define a comparison between 2\nvalues. This could be EQUAL, LESS_THEN, BIGGER_OR_EQUAL_THEN,... The different choices are available on the class\nitself. A good example of this is\navailable ",(0,o.kt)("a",{parentName:"p",href:"https://github.com/titivermeesch/CommandTimer_PlayerConditions/blob/master/src/main/java/me/playbosswar/cmtplayerconditions/conditions/PlayerTimeInWorldCondition.java"},"here")),(0,o.kt)("h3",{id:"examples"},"Examples"),(0,o.kt)("p",null,"A few examples can be found here:"),(0,o.kt)("ul",null,(0,o.kt)("li",{parentName:"ul"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/titivermeesch/CommandTimer_TimeConditions"},"https://github.com/titivermeesch/CommandTimer_TimeConditions")),(0,o.kt)("li",{parentName:"ul"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/titivermeesch/CommandTimer_GriefDefender"},"https://github.com/titivermeesch/CommandTimer_GriefDefender")),(0,o.kt)("li",{parentName:"ul"},(0,o.kt)("a",{parentName:"li",href:"https://github.com/titivermeesch/CommandTimer_PlayerConditions"},"https://github.com/titivermeesch/CommandTimer_PlayerConditions"))))}p.isMDXComponent=!0}}]);