import React from 'react';

const TextBox = () => {
    return (
        <textarea
            className="bg-white h-72 w-full rounded-xl
            shadow-[-5px_-5px_15px_#62467d_inset,_-20px_-20px_15px_rgba(0,_0,_0,_0.15)_inset,_20px_20px_15px_rgba(255,_255,_255,_0.8)_inset] p-4
            text-[533A6B] focus:outline-none"
            rows={14}
            cols={19}
        />
    );
};

export default TextBox;
