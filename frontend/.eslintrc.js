module.exports = {
  root: true,
  env: {
    node: true,
  },
  extends: [
    'plugin:vue/essential',
    '@vue/airbnb',
  ],
  rules: {
    'no-console': process.env.NODE_ENV === 'production' ? 'error' : 'off',
    'no-debugger': process.env.NODE_ENV === 'production' ? 'error' : 'off',
    'max-len': 0,
    'linebreak-style': "off",
    'max-params': "off",
    'no-param-reassign': "off",
    radix: "off",
    'no-trailing-spaces': "off",
    'no-shadow': "off",
    'no-unused-vars': "off",
    'no-plusplus': "off",
    'no-restricted-syntax': "off",
    'comma-dangle': "off",
    'comma-style': "off",
    'comma-spacing': "off",
    'object-shorthand': "off",
    'func-names': "off",
    semi: "off",
    'object-curly-newline': "off",
    'space-before-function-paren': "off",
    'global-require': "off",
    indent: "off",
    'indent-legacy': "off",
    'no-unused-labels': "off",
    'no-unused-expressions': "off",
  },
  parserOptions: {
    parser: 'babel-eslint',
  },
  overrides: [
    {
      files: [
        '**/__tests__/*.{j,t}s?(x)',
      ],
      env: {
        mocha: true,
      },
    },
  ],
};
