import '@/styles/globals.css';
import type { AppProps } from 'next/app';
import { hanna } from '@/styles/font';

export default function App({ Component, pageProps }: AppProps) {
    return (
        <main className={`${hanna.variable} font-hanna`}>
            <Component {...pageProps} />
        </main>
    );
}
