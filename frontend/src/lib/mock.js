export const providers = ['GitHub', 'GitLab', 'Bitbucket'];

export const user = { name: 'elazar', avatar: 'https://github.com/github.png' };

export const availableRepos = [
    { id: 1, name: 'elazar/pr-review-bot', origin: 'GitHub' },
    { id: 2, name: 'elazar/compilateur', origin: 'GitHub' },
    { id: 3, name: 'elazar/graph-algos', origin: 'GitHub' },
];

export const connectedRepos = [
    { id: 1, name: 'elazar/pr-review-bot', origin: 'GitHub', prCount: 12, webhook: 'actif' },
    { id: 2, name: 'elazar/compilateur', origin: 'GitHub', prCount: 5, webhook: 'erreur' },
];

export const reviews = [
    { id: 1, pr: '#42 Ajout du parser', repo: 'pr-review-bot', status: 'terminée', comments: 7, blocking: true, date: '2026-10-03' },
    { id: 2, pr: '#41 Fix NPE', repo: 'pr-review-bot', status: 'en cours', comments: 0, blocking: false, date: '2026-10-04' },
    { id: 3, pr: '#8 Optimisation lexer', repo: 'compilateur', status: 'terminée', comments: 3, blocking: false, date: '2026-10-02' },
    { id: 4, pr: '#7 Refactor AST', repo: 'compilateur', status: 'échec', comments: 0, blocking: false, date: '2026-10-01' },
    { id: 5, pr: '#43 Tests unitaires', repo: 'pr-review-bot', status: 'en attente', comments: 0, blocking: false, date: '2026-10-04' },
];