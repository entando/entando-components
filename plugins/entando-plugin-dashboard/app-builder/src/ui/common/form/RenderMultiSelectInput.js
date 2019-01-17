import React from "react";
import {Typeahead, Token, tokenContainer} from "react-bootstrap-typeahead";

const RenderMultiSelectInput = ({
  input,
  options,
  align,
  className,
  maxHeight
}) => {
  return (
    <Typeahead
      {...input}
      align={align}
      multiple
      renderToken={(option, props, index) => {
        console.log("rendeToken option", option);
        console.log("rendeToken props", props);
        console.log("rendeToken index", index);
        return (
          <Token key={index} onRemove={props.onRemove}>
            {option.label}
          </Token>
        );
      }}
      options={options}
      maxHeight={maxHeight}
      className={className}
      selectHintOnEnter
      onBlur={() => {}}
      onInputChange={input => {
        console.log("onInputChange", input);
      }}
      onChange={val => {
        console.log("val", val);
        const value = val.map(x => x.value);
        console.log("value", value);
        input.onChange(value);
      }}
    />
  );
};

export default RenderMultiSelectInput;
