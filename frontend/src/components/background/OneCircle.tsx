import FlexBox from '../layout/FlexBox';

export default function OneCircle() {
    return (
        <FlexBox className="justify-center w-full">
            <div
                style={{
                    width: 300,
                    height: 650,
                    background: 'rgba(58, 37, 185, 32%)',
                    filter: 'blur(28px)',
                    borderRadius: '50%',
                }}
            />
        </FlexBox>
    );
}
