import React from 'react';
import styled from 'styled-components';

const Switch = ({ onClick }) => {
    return (
        <StyledWrapper>
            <div>
                <input id="checkbox" type="checkbox" />
                <label className="toggle" htmlFor="checkbox" onClick={onClick}>
                    <div id="bar1" className="bars" />
                    <div id="bar2" className="bars" />
                    <div id="bar3" className="bars" />
                </label>
            </div>
        </StyledWrapper>
    );
}

const StyledWrapper = styled.div`
    #checkbox {
        display: none;
    }

    .toggle {
        position: relative;
        width: 30px;
        height: 30px;
        cursor: pointer;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        justify-self: flex-end;
        margin-right: -50px;
        background-color: rgb(34, 89, 172);
        gap: 10px;
        padding: 1rem;
        border-radius: 10px;
        transition-duration: .3s;
    }

    .bars {
        width: 100%;
        height: 4px;
        background-color: rgb(247, 243, 255);
        border-radius: 5px;
        transition-duration: .3s;
    }

    /* #checkbox:checked + .toggle .bars {
      margin-left: 13px;
    } */

    #checkbox:checked + .toggle #bar2 {
        transform: translateY(14px) rotate(60deg);
        margin-left: 0;
        transform-origin: right;
        transition-duration: .3s;
        z-index: 2;
    }

    #checkbox:checked + .toggle #bar1 {
        transform: translateY(28px) rotate(-60deg);
        transition-duration: .3s;
        transform-origin: left;
        z-index: 1;
    }

    #checkbox:checked + .toggle {
        transform: rotate(-90deg);
    }

    /* #checkbox:checked + .toggle #bar3 {
      transform: rotate(90deg);
      transition-duration: .3s;
      transform-origin:right;
    } */`;

export default Switch;
