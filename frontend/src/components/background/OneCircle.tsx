import FlexBox from '../layout/FlexBox';

export default function OneCircle() {
    return (
        <FlexBox className="justify-center h-screen">
            <div
                style={{
                    width: 300,
                    height: 650,
                    background: 'rgba(58, 37, 185, 15%)',
                    filter: 'blur(28px)',
                    borderRadius: '50%',
                }}
            />
        </FlexBox>
    );
}
