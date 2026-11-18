# ERP — Module Fournisseur

Projet de semestre, matière ERP. Le module Fournisseur gère tout le cycle d'achat de l'entreprise : du besoin d'achat jusqu'au fournisseur payé.

**Équipe :** Maram Chaouch · Eya Touati · Shahd Kdaiem · Eya Ferah

| Sous-processus | Rôle |
|---|---|
| A · Fournisseurs et catalogue | Créer, évaluer ou désactiver un fournisseur |
| B · Consultation | Du stock bas à l'offre retenue |
| C · Commande et réception | De l'offre retenue à la réception validée |
| D · Facturation et paiement | De la réception au fournisseur payé |

**Technologies :** Java 17 · Spring Boot 3 · PostgreSQL · Thymeleaf · Bootstrap

## Documents

| Document | Lien |
|---|---|
| Conception BPMN | [Ouvrir le PDF des diagrammes](docs/01-conception/diagrammes-bpmn.pdf) · [Source draw.io](docs/01-conception/moduleFournisseur.drawio) |
| Présentation | [Ouvrir le PDF](docs/02-presentation/Presentation.pdf) |
| Choix des technologies | [Ouvrir le PDF](docs/04-technologies/choix-des-technologies.pdf) |

## Organisation du dépôt

```
docs/
├── 01-conception/      diagrammes BPMN (.drawio, PDF, captures)
├── 02-presentation/    présentation (PDF et slides)
└── 04-technologies/    choix des technologies
src/                    code Spring Boot (à venir)
```

## Conception BPMN

Un workflow (l'achat), découpé en 4 sous-processus : A, B, C et D.

### Niveau 1 — vue globale
![Niveau 1](docs/01-conception/capturesDesDiag/niveau-1.png)

### Vue complète
![Vue complète](docs/01-conception/capturesDesDiag/vue-complete.png)

### A — Fournisseurs et catalogue
![Sous-processus A](docs/01-conception/capturesDesDiag/A.png)

### B — Consultation
![Sous-processus B](docs/01-conception/capturesDesDiag/B.png)

### C — Commande et réception
![Sous-processus C](docs/01-conception/capturesDesDiag/C.png)

### D — Facturation et paiement
![Sous-processus D](docs/01-conception/capturesDesDiag/D.png)

## Présentation

![Slide 1](docs/02-presentation/slides/slide-01.jpg)
![Slide 2](docs/02-presentation/slides/slide-02.jpg)
![Slide 3](docs/02-presentation/slides/slide-03.jpg)
![Slide 4](docs/02-presentation/slides/slide-04.jpg)
![Slide 5](docs/02-presentation/slides/slide-05.jpg)
![Slide 6](docs/02-presentation/slides/slide-06.jpg)
![Slide 7](docs/02-presentation/slides/slide-07.jpg)
![Slide 8](docs/02-presentation/slides/slide-08.jpg)
![Slide 9](docs/02-presentation/slides/slide-09.jpg)
![Slide 10](docs/02-presentation/slides/slide-10.jpg)
![Slide 11](docs/02-presentation/slides/slide-11.jpg)
![Slide 12](docs/02-presentation/slides/slide-12.jpg)
![Slide 13](docs/02-presentation/slides/slide-13.jpg)
![Slide 14](docs/02-presentation/slides/slide-14.jpg)
![Slide 15](docs/02-presentation/slides/slide-15.jpg)
![Slide 16](docs/02-presentation/slides/slide-16.jpg)
