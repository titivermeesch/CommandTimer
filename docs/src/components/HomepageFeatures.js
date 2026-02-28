import React from 'react';
import clsx from 'clsx';
import Link from '@docusaurus/Link';
import styles from './HomepageFeatures.module.css';

const FeatureList = [
  {
    title: 'Schedule Commands',
    icon: '\u23F0',
    description: 'Set up commands to run on intervals, at specific times of day, or on specific dates. Full control over when and how your commands execute.',
    link: '/docs/configuration/commands',
  },
  {
    title: 'Powerful Conditions',
    icon: '\u2699\uFE0F',
    description: 'Execute commands only when specific conditions are met. Filter by player count, time of day, weather, permissions, and more.',
    link: '/docs/configuration/conditions',
  },
  {
    title: 'Task Management',
    icon: '\uD83D\uDCCB',
    description: 'Organize commands into tasks with their own schedules and settings. Manage everything through an intuitive in-game GUI.',
    link: '/docs/configuration/commands',
  },
  {
    title: 'Extensible',
    icon: '\uD83E\uDDE9',
    description: 'Extend functionality with custom extensions. Add new conditions, execution modes, and placeholders to fit your server needs.',
    link: '/docs/extensions/',
  },
  {
    title: 'Placeholders',
    icon: '\uD83D\uDD04',
    description: 'Use dynamic placeholders in your commands. Supports PlaceholderAPI and built-in placeholders for flexible command templates.',
    link: '/docs/placeholders',
  },
  {
    title: 'Developer API',
    icon: '\uD83D\uDEE0\uFE0F',
    description: 'Integrate CommandTimer into your own plugins with a clean API. Available through Maven and Gradle for easy setup.',
    link: '/docs/developers/',
  },
];

function Feature({ title, icon, description, link }) {
  return (
    <div className={clsx('col col--4', styles.featureCol)}>
      <Link to={link} className={styles.featureCard}>
        <div className={styles.featureIcon}>{icon}</div>
        <h3 className={styles.featureTitle}>{title}</h3>
        <p className={styles.featureDescription}>{description}</p>
      </Link>
    </div>
  );
}

export default function HomepageFeatures() {
  return (
    <section className={styles.features}>
      <div className="container">
        <div className={clsx('row', styles.featureRow)}>
          {FeatureList.map((props, idx) => (
            <Feature key={idx} {...props} />
          ))}
        </div>
      </div>
    </section>
  );
}
