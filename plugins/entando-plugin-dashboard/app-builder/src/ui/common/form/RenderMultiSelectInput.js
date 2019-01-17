import React from "react";
import {Typeahead, Token, tokenContainer} from "react-bootstrap-typeahead";

const RenderMultiSelectInput = ({
  input,
  options,
  align,
  className,
  maxHeight
}) => {
  // const MyCustomToken = tokenContainer(props => {
  //   console.log("MyCustomToken props", props);
  //   return (
  //     <div
  //       {...props}
  //       onClick={event => {
  //         props.onRemove();
  //         event.stopPropagation();
  //         props.onClickCustom(event);
  //       }}
  //       className="rbt-token rbt-token-removeable RenderMultiSelectInput__custom-token"
  //       tabIndex={0}
  //     >
  //       {props.children}
  //       <span onClick={() => props.onRemove()}>
  //         <i className="fa fa-times crossButton" />
  //       </span>
  //     </div>
  //   );
  // });

  return (
    <Typeahead
      {...input}
      align={align}
      multiple
      // renderToken={(option, props, index) => {
      //   return (
      //     <MyCustomToken
      //       key={index}
      //       onRemove={props.onRemove}
      //       onClickCustom={event => {
      //         console.log("onClickCustom ", event);
      //       }}
      //     >
      //       {`${option.label}`}
      //     </MyCustomToken>
      //   );
      // }}
      options={options}
      maxHeight={maxHeight}
      className={className}
      onBlur={() => {}}
      onChange={val => {
        const value = val.map(x => ({...x}));
        input.onChange(value);
      }}
    />
  );
};

export default RenderMultiSelectInput;
