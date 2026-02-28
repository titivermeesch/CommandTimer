import React from 'react';
import clsx from 'clsx';
import Link from '@docusaurus/Link';
import useDocusaurusContext from '@docusaurus/useDocusaurusContext';
import Layout from '@theme/Layout';
import HomepageFeatures from '@site/src/components/HomepageFeatures';

import styles from './index.module.css';

function HomepageHeader() {
    const {siteConfig} = useDocusaurusContext();
    return (
        <header className={clsx('hero', styles.heroBanner)}>
            <div className="container">
                <img
                    src="img/logo.jpg"
                    alt="CommandTimer Logo"
                    className={styles.heroLogo}
                />
                <h1 className="hero__title">{siteConfig.title}</h1>
                <p className="hero__subtitle">{siteConfig.tagline}</p>
                <div className={styles.buttons}>
                    <Link
                        className="button button--primary button--lg"
                        to="/docs/intro">
                        Get Started
                    </Link>
                    <Link
                        className="button button--outline button--lg"
                        to="https://github.com/titivermeesch/CommandTimer">
                        View on GitHub
                    </Link>
                </div>
            </div>
        </header>
    );
}

function QuickStart() {
    return (
        <section className={styles.quickStart}>
            <div className="container">
                <h2 className={styles.sectionTitle}>Quick Start</h2>
                <div className={styles.steps}>
                    <div className={styles.step}>
                        <div className={styles.stepNumber}>1</div>
                        <h3>Install the Plugin</h3>
                        <p>Download CommandTimer and drop it into your server's plugins folder.</p>
                    </div>
                    <div className={styles.step}>
                        <div className={styles.stepNumber}>2</div>
                        <h3>Create a Task</h3>
                        <p>Use <code>/cmt</code> to open the GUI and create your first task.</p>
                    </div>
                    <div className={styles.step}>
                        <div className={styles.stepNumber}>3</div>
                        <h3>Add Commands</h3>
                        <p>Configure commands, set schedules, and let CommandTimer handle the rest.</p>
                    </div>
                </div>
            </div>
        </section>
    );
}

export default function Home() {
    return (
        <Layout description="Schedule commands like you want">
            <HomepageHeader />
            <main>
                <HomepageFeatures />
                <QuickStart />
            </main>
        </Layout>
    );
}
