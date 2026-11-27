// Bloc A : membre 1
export const menuA = [
  { chemin: '/a/fournisseurs', titre: 'Fournisseurs', roles: ['ACHATS', 'DIRECTEUR'] },
]

// Bloc B : membre 2
export const menuB = [
  { chemin: '/b/demandes', titre: "Demandes d'achat", roles: ['MAGASINIER', 'ACHATS'] },
]

// Bloc C : membre 3
export const menuC = [
  { chemin: '/c/commandes', titre: 'Commandes', roles: ['ACHATS', 'DIRECTEUR', 'MAGASINIER'] },
  { chemin: '/c/validations', titre: 'Validation', roles: ['DIRECTEUR'] },
  { chemin: '/c/receptions', titre: 'Réception', roles: ['MAGASINIER'] },
  { chemin: '/c/reclamations', titre: 'Réclamations', roles: ['MAGASINIER', 'ACHATS'] },
]

// Bloc D : membre 4 (toi)
export const menuD = [
  { chemin: '/d/factures', titre: 'Factures', roles: ['COMPTABLE'] },
  { chemin: '/d/litiges', titre: 'Litiges', roles: ['COMPTABLE'] },
  { chemin: '/d/tableau-de-bord', titre: 'Tableau de bord', roles: ['COMPTABLE'] },
]

export default [...menuA, ...menuB, ...menuC, ...menuD]