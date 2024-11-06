import '@/styles/globals.css';
import type { AppProps } from 'next/app';
import { hanna } from '@/styles/font';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';

const queryClient = new QueryClient();

export default function App({ Component, pageProps }: AppProps) {
    return (
        <QueryClientProvider client={queryClient}>
            <main className={`${hanna.variable} font-hanna`}>
                <Component {...pageProps} />
            </main>
        </QueryClientProvider>
    );
}
