// @ts-check
// Note: type annotations allow type checking and IDEs autocompletion

const lightCodeTheme = require('prism-react-renderer/themes/github');
const darkCodeTheme = require('prism-react-renderer/themes/dracula');

/** @type {import('@docusaurus/types').Config} */
const config = {
    title: 'CommandTimer',
    tagline: 'Schedule commands like you want',
    url: "https://pages.github.com",
    baseUrl: '/',
    onBrokenLinks: 'throw',
    onBrokenMarkdownLinks: 'warn',
    favicon: 'img/favicon.ico',
    organizationName: 'commandtimer',
    projectName: 'commandtimer',

    i18n: {
        defaultLocale: 'en', locales: ['en'],
    },

    presets: [['classic', /** @type {import('@docusaurus/preset-classic').Options} */
        ({
            docs: {
                sidebarPath: require.resolve('./sidebars.js'), // Please change this to your repo.
                // Remove this to remove the "edit this page" links.
                editUrl: 'https://github.com/facebook/docusaurus/tree/main/packages/create-docusaurus/templates/shared/',
            }, blog: {
                showReadingTime: true, // Please change this to your repo.
                // Remove this to remove the "edit this page" links.
                editUrl: 'https://github.com/facebook/docusaurus/tree/main/packages/create-docusaurus/templates/shared/',
            }, theme: {
                customCss: require.resolve('./src/css/custom.css'),
            },
        }),],],

    themeConfig: /** @type {import('@docusaurus/preset-classic').ThemeConfig} */
        ({
            navbar: {
                title: 'CommandTimer', logo: {
                    alt: 'CommandTimer Logo', src: 'img/logo.svg',
                }, items: [{
                    type: 'doc', docId: 'intro', position: 'left', label: 'Documentation',
                }, {
                    href: 'https://github.com/titivermeesch/CommandTimer', label: 'GitHub', position: 'right',
                },],
            }, footer: {
                style: 'dark', links: [{
                    title: 'Community', items: [{
                        label: 'Discord', href: 'https://discord.gg/3DUPq3y',
                    },],
                }, {
                    title: 'More', items: [{
                        label: 'GitHub', href: 'https://github.com/titivermeesch/CommandTimer',
                    },],
                },], copyright: `Copyright © ${new Date().getFullYear()} CommandTimer, Inc. Built with Docusaurus.`,
            }, prism: {
                theme: lightCodeTheme, darkTheme: darkCodeTheme,
            },
        }),
};

module.exports = config;
