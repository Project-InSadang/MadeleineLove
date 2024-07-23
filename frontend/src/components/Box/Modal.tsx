import FlexBox from '../layout/FlexBox';

interface ModalProps {
    description: string;
    buttonCount: 'one' | 'two';
    isLogin?: boolean;
    onCancle: () => void;
    onConfirm?: () => void;
}

export default function Modal({
    description,
    buttonCount,
    isLogin = false,
    onCancle,
    onConfirm,
}: ModalProps) {
    return (
        <div className="z-50">
            <div className="fixed inset-0 bg-gray-500 opacity-50"></div>
            <div className="fixed inset-0 flex items-center justify-center">
                <div
                    className="bg-[#4B4189] h-[145px] w-[255px]
                rounded-2xl shadow-lg"
                >
                    <FlexBox
                        direction="col"
                        className="w-full h-full text-white justify-center items-center
                        text-lg gap-5"
                    >
                        {description}

                        <FlexBox direction="row" className="gap-4">
                            {buttonCount === 'one' ? (
                                <button
                                    className="w-[75px] text-black bg-white text-base py-0.5 rounded-3xl"
                                    onClick={onCancle}
                                >
                                    확인
                                </button>
                            ) : (
                                <>
                                    <button
                                        className="w-[75px] text-black bg-white text-base py-0.5 rounded-3xl"
                                        onClick={onConfirm}
                                    >
                                        {isLogin ? '로그인' : '예'}
                                    </button>
                                    <button
                                        className="w-[75px] text-black bg-white text-base py-0.5 rounded-3xl"
                                        onClick={onCancle}
                                    >
                                        {isLogin ? '취소' : '아니오'}
                                    </button>
                                </>
                            )}
                        </FlexBox>
                    </FlexBox>
                </div>
            </div>
        </div>
    );
}
