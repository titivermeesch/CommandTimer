// @ts-check
// Note: type annotations allow type checking and IDEs autocompletion

const lightCodeTheme = require("prism-react-renderer/themes/github");
const darkCodeTheme = require("prism-react-renderer/themes/dracula");

/** @type {import('@docusaurus/types').Config} */
const config = {
  title: "CommandTimer",
  tagline: "Schedule commands like you want",
  url: "https://pages.github.com",
  baseUrl: "/",
  onBrokenLinks: "throw",
  onBrokenMarkdownLinks: "warn",
  favicon: "img/favicon.ico",
  organizationName: "titivermeesch",
  projectName: "commandtimer",
  trailingSlash: false,
  i18n: {
    defaultLocale: "en",
    locales: ["en"],
  },
  presets: [
    [
      "classic",
      /** @type {import('@docusaurus/preset-classic').Options} */
      ({
        docs: {
          sidebarPath: require.resolve("./sidebars.js"), // Please change this to your repo.
        },
        blog: {
          showReadingTime: true, // Please change this to your repo.
        },
        theme: {
          customCss: require.resolve("./src/css/custom.css"),
        },
      }),
    ],
  ],

  themeConfig:
    /** @type {import('@docusaurus/preset-classic').ThemeConfig} */
    ({
      navbar: {
        title: "CommandTimer",
        logo: {
          alt: "CommandTimer Logo",
          src: "img/logo.svg",
        },
        items: [
          {
            type: "doc",
            docId: "intro",
            position: "left",
            label: "Documentation",
          },
          {
            type: "doc",
            docId: "extensions/index",
            position: "left",
            label: "Extensions",
          },
          {
            type: "doc",
            docId: "developers/index",
            position: "left",
            label: "Developers",
          },
          {
            href: "https://discord.gg/3DUPq3y",
            label: "Discord",
            position: "right",
          },
          {
            href: "https://github.com/titivermeesch/CommandTimer",
            label: "GitHub",
            position: "right",
          },
        ],
      },
      footer: {
        style: "dark",
        links: [
          {
            title: "Community",
            items: [
              {
                label: "Discord",
                href: "https://discord.gg/3DUPq3y",
              },
            ],
          },
          {
            title: "More",
            items: [
              {
                label: "GitHub",
                href: "https://github.com/titivermeesch/CommandTimer",
              },
            ],
          },
        ],
        copyright: `Copyright © ${new Date().getFullYear()} CommandTimer`,
      },
      prism: {
        theme: lightCodeTheme,
        darkTheme: darkCodeTheme,
      },
    }),
};

module.exports = config;
