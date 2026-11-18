// @ts-check
// Note: type annotations allow type checking and IDEs autocompletion

const { themes } = require("prism-react-renderer");

const lightCodeTheme = themes.github;
const darkCodeTheme = themes.dracula;

/** @type {import('@docusaurus/types').Config} */
const config = {
  title: "CommandTimer",
  tagline: "Schedule commands like you want",
  url: "https://pages.github.com",
  baseUrl: "/CommandTimer/",
  onBrokenLinks: "throw",
  favicon: "img/logo.jpg",
  organizationName: "titivermeesch",
  projectName: "commandtimer",
  trailingSlash: false,
  markdown: {
    format: "detect",
    hooks: {
      onBrokenMarkdownLinks: "warn",
    },
  },
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
          sidebarPath: require.resolve("./sidebars.js"),
        },
        blog: {
          showReadingTime: true,
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
          src: "img/logo.jpg",
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
          {
            href: "https://patreon.com/commandtimer",
            label: "Patreon",
            position: "right"
          }
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
