// <div className="flex h-screen">

import FlexBox from '../layout/FlexBox';

export default function TwoCircle() {
    return (
        <FlexBox direction="col" className="w-full justify-center gap-10">
            <div className="mt-10">
                <div
                    className="rounded-full mr-20"
                    style={{
                        width: 300,
                        height: 300,
                        background: '#8271C9',
                        filter: 'blur(60px)',
                    }}
                ></div>
                <div
                    className="rounded-full ml-20"
                    style={{
                        width: 300,
                        height: 300,
                        background: '#8C7ECE',
                        filter: 'blur(55px)',
                    }}
                ></div>
            </div>
        </FlexBox>
    );
}
