import React from 'react';
import { PropTypes } from 'prop-types';
import greenCheck from 'assets/green-check.png';

import FormattedMessage from 'ui/i18n/FormattedMessage';

import plugin from 'index-plugin';


class PluginStatus extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      reduxOk: props.reduxOk,
      publicAssetsOk: null,
      importedAssetsOk: null,
      cssOk: null,
      translationOk: null,
    };
  }

  renderPublicAssetsTest() {
    if (!this.props.publicAssetFileName) {
      return null;
    }
    return (
      <img
        alt="FAILED"
        style={{ display: 'none' }}
        src={`plugin-assets/${plugin.id}/${this.props.publicAssetFileName}`}
        onLoad={() => { this.setState({ publicAssetsOk: true }); }}
        onError={() => { this.setState({ publicAssetsOk: false }); }}
      />
    );
  }

  renderImportedAssetsTest() {
    return (
      <img
        alt="FAILED"
        style={{ display: 'none' }}
        src={greenCheck}
        onLoad={() => { this.setState({ importedAssetsOk: true }); }}
        onError={() => { this.setState({ importedAssetsOk: false }); }}
      />
    );
  }

  renderCssTest() {
    return (
      <span
        className="PluginStatus--css-test"
        style={{ display: 'block', height: '1px' }}
        ref={(el) => {
          if (el && this.state.cssOk === null) {
            this.setState({ cssOk: (window.getComputedStyle(el).height === '2px') });
          }
        }}
      />
    );
  }

  renderTranslationTest() {
    return (
      <span
        style={{ display: 'none' }}
        ref={(el) => {
          if (el && this.state.translationOk === null) {
            this.setState({ translationOk: el.textContent === 'ok' });
          }
        }}
      >
        <FormattedMessage id="PluginStatus.test" />
      </span>
    );
  }

  renderCheckListItem(propName, title) {
    const statusClass = this.state[propName]
      ? 'PluginStatus--list-elem__success'
      : 'PluginStatus--list-elem__failure';
    return (
      <li className={`PluginStatus--list-elem ${statusClass}`}>
        <b className="PluginStatus--list-elem-label">{title}:</b>
        <span className="PluginStatus--list-elem-status">
          { this.state[propName] ? 'working' : 'not working' }
        </span>
      </li>
    );
  }

  render() {
    return (
      <div className="PluginStatus">
        <h1>Plugin status: <small>{plugin.id}</small></h1>
        <ul>
          { this.renderCheckListItem('reduxOk', 'Redux state manager')}
          { this.props.publicAssetFileName && this.renderCheckListItem('publicAssetsOk', 'Public assets')}
          { this.renderCheckListItem('importedAssetsOk', 'Imported assets')}
          { this.renderCheckListItem('cssOk', 'Plugin CSS')}
          { this.renderCheckListItem('translationOk', 'Translation')}
        </ul>
        { this.renderPublicAssetsTest() }
        { this.renderImportedAssetsTest() }
        { this.renderCssTest() }
        { this.renderTranslationTest() }
      </div>
    );
  }
}

PluginStatus.defaultProps = {
  reduxOk: false,
  publicAssetFileName: '',
};

PluginStatus.propTypes = {
  reduxOk: PropTypes.bool,
  publicAssetFileName: PropTypes.string,
};

export default PluginStatus;
