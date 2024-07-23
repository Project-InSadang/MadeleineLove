interface TextProps {
    height: number;
    className?: string;
    placeholder?: string;
    onChange: (e: React.ChangeEvent<HTMLTextAreaElement>) => void;
}

export default function TextBox({
    className,
    height,
    placeholder,
    onChange,
}: TextProps) {
    return (
        <textarea
            className={`w-[298px]
            bg-white text-base focus:outline-none resize-none
            shadow-[-3px_-3px_15px_#62467d_inset,-5px_-5px_7px_rgba(0,_0,_0,_0.15)_inset]
            ${className}`}
            style={{ height: `${height}px`, overflow: 'hidden' }}
            placeholder={placeholder}
            onChange={onChange}
        />
    );
}

//max width 정하기
