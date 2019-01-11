import React, {Fragment} from "react";
import {Wizard} from "patternfly-react";
import {formattedText} from "@entando/utils";

const ChartSteps = ({stepIndex}) => (
  <Fragment>
    <Wizard.Step
      key={0}
      stepIndex={0}
      step={1}
      label="1"
      title={formattedText("plugin.charts.firstStep")}
      activeStep={stepIndex + 1}
      className="ChartSteps__step"
    />
    <Wizard.Step
      key={1}
      stepIndex={1}
      step={2}
      label="2"
      title={formattedText("plugin.charts.secondStep")}
      activeStep={stepIndex + 1}
      className="ChartSteps__step"
    />
    <Wizard.Step
      key={2}
      stepIndex={2}
      step={3}
      label="3"
      title={formattedText("plugin.charts.thirdStep")}
      activeStep={stepIndex + 1}
      className="ChartSteps__step"
    />
  </Fragment>
);

export default ChartSteps;
