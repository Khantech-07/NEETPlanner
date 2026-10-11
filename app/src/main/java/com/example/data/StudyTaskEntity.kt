package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "study_tasks")
data class StudyTaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val dayIndex: Int,
    val weekTitle: String,
    val dateLabel: String,
    val fullDate: String,
    val subject: String,
    val chapter: String,
    val notes: String = "",
    val targetQuestions: Int = 0,
    val practicedQuestions: Int = 0,
    val isCompleted: Boolean = false,
    val isMilestone: Boolean = false,
    val reminderTimeMillis: Long = 0L
)

object InitialScheduleData {
    fun getInitialTasks(): List<StudyTaskEntity> {
        val list = mutableListOf<StudyTaskEntity>()
        var day = 1

        fun addTask(
            week: String,
            dateLabel: String,
            fullDate: String,
            subject: String,
            chapter: String,
            notes: String = "",
            targetQ: Int = 0,
            isMilestone: Boolean = false
        ) {
            list.add(
                StudyTaskEntity(
                    id = 0,
                    dayIndex = day,
                    weekTitle = week,
                    dateLabel = dateLabel,
                    fullDate = fullDate,
                    subject = subject,
                    chapter = chapter,
                    notes = notes,
                    targetQuestions = targetQ,
                    practicedQuestions = 0,
                    isCompleted = false,
                    isMilestone = isMilestone
                )
            )
        }

        // =========================================================================
        // Week 1: 12–18 October
        // =========================================================================
        addTask("12–18 October", "Mon 12", "Mon, 12 Oct 2026", "Physics", "Mathematical Tools", "", 25)
        addTask("12–18 October", "Mon 12", "Mon, 12 Oct 2026", "Botany", "The Living World", "current NCERT only", 30)
        day++

        addTask("12–18 October", "Tue 13", "Tue, 13 Oct 2026", "Chemistry", "Some Basic Concepts", "Mole, stoichiometry, limiting reagent, molarity, molality, mole fraction", 30)
        day++

        addTask("12–18 October", "Wed 14", "Wed, 14 Oct 2026", "Botany", "Cell", "", 40)
        addTask("12–18 October", "Wed 14", "Wed, 14 Oct 2026", "Zoology", "Structural Organisation", "tissues + frog. Long day", 35)
        day++

        addTask("12–18 October", "Thu 15", "Thu, 15 Oct 2026", "Mock Test", "Yakeen NEET-1", "Official paper", 0)
        day++

        addTask("12–18 October", "Fri 16", "Fri, 16 Oct 2026", "Physics", "Vectors and Units and Measurements", "25 + 20 question target", 45)
        day++

        addTask("12–18 October", "Sat 17", "Sat, 17 Oct 2026", "Mock Test", "NRTS 01", "Official paper", 0)
        day++

        addTask("12–18 October", "Sun 18", "Sun, 18 Oct 2026", "Physics", "Motion in a Straight Line", "", 25)
        addTask("12–18 October", "Sun 18", "Sun, 18 Oct 2026", "Chemistry", "Redox", "", 25)
        day++

        // =========================================================================
        // Week 2: 19–25 October
        // =========================================================================
        addTask("19–25 October", "Mon 19", "Mon, 19 Oct 2026", "Mock Test", "Yakeen NEET-2", "Official paper", 0)
        day++

        addTask("19–25 October", "Tue 20", "Tue, 20 Oct 2026", "Chemistry", "Solutions", "", 25)
        addTask("19–25 October", "Tue 20", "Tue, 20 Oct 2026", "Botany", "Cell Cycle and Cell Division", "", 35)
        day++

        addTask("19–25 October", "Wed 21", "Wed, 21 Oct 2026", "Mock Test", "Yakeen Rank Booster 1", "Official paper", 0)
        day++

        addTask("19–25 October", "Thu 22", "Thu, 22 Oct 2026", "Chemistry", "Thermodynamics and Thermochemistry", "Hess, entropy, Gibbs", 30)
        day++

        addTask("19–25 October", "Fri 23", "Fri, 23 Oct 2026", "Zoology", "Breathing and Exchange of Gases", "", 40)
        addTask("19–25 October", "Fri 23", "Fri, 23 Oct 2026", "Chemistry", "Chemical Equilibrium", "", 25)
        day++

        addTask("19–25 October", "Sat 24", "Sat, 24 Oct 2026", "Mock Test", "Yakeen NEET-3", "Official paper", 0)
        day++

        addTask("19–25 October", "Sun 25", "Sun, 25 Oct 2026", "Physics", "Motion in a Plane", "Projectile and uniform circular motion", 30)
        day++

        // =========================================================================
        // Week 3: 26 October – 1 November
        // =========================================================================
        addTask("26 October – 1 November", "Mon 26", "Mon, 26 Oct 2026", "Botany", "Biological Classification", "Heaviest double of October", 35)
        addTask("26 October – 1 November", "Mon 26", "Mon, 26 Oct 2026", "Zoology", "Body Fluids and Circulation", "Heaviest double of October", 35)
        day++

        addTask("26 October – 1 November", "Tue 27", "Tue, 27 Oct 2026", "Mock Test", "NRTS 02", "Official paper", 0)
        day++

        addTask("26 October – 1 November", "Wed 28", "Wed, 28 Oct 2026", "Chemistry", "Ionic Equilibrium", "pH, buffers, Ksp, hydrolysis", 30)
        day++

        addTask("26 October – 1 November", "Thu 29", "Thu, 29 Oct 2026", "Mock Test", "Yakeen NEET-4", "Official paper", 0)
        day++

        addTask("26 October – 1 November", "Fri 30", "Fri, 30 Oct 2026", "Chemistry", "Electrochemistry", "Conductance, Nernst, Faraday", 30)
        day++

        addTask("26 October – 1 November", "Sat 31", "Sat, 31 Oct 2026", "Mock Test", "NRTS 03", "Official paper", 0)
        day++

        addTask("26 October – 1 November", "Sun 1 Nov", "Sun, 01 Nov 2026", "Physics", "Laws of Motion", "FBD, friction, banking. Cockroach account only if your AITS module still asks it", 30)
        day++

        // =========================================================================
        // Week 4: 2–8 November
        // =========================================================================
        addTask("2–8 November", "Mon 2", "Mon, 02 Nov 2026", "Mock Test", "Yakeen AITS-1", "Official paper", 0)
        day++

        addTask("2–8 November", "Tue 3", "Tue, 03 Nov 2026", "Chemistry", "Chemical Kinetics", "", 25)
        addTask("2–8 November", "Tue 3", "Tue, 03 Nov 2026", "Botany", "Plant Kingdom", "", 40)
        day++

        addTask("2–8 November", "Wed 4", "Wed, 04 Nov 2026", "Mock Test", "Yakeen Rank Booster 2", "Official paper", 0)
        day++

        addTask("2–8 November", "Thu 5", "Thu, 05 Nov 2026", "Zoology", "Excretory Products", "", 40)
        day++

        addTask("2–8 November", "Fri 6", "Fri, 06 Nov 2026", "Mock Test", "NRTS 04", "Official paper", 0)
        day++

        addTask("2–8 November", "Sat 7", "Sat, 07 Nov 2026", "Chemistry", "Structure of Atom", "", 25)
        addTask("2–8 November", "Sat 7", "Sat, 07 Nov 2026", "Zoology", "Locomotion and Movement", "", 40)
        day++

        addTask("2–8 November", "Sun 8", "Sun, 08 Nov 2026", "Botany", "Morphology", "Only the families still printed in your book", 45)
        day++

        // =========================================================================
        // Week 5: 9–15 November
        // =========================================================================
        addTask("9–15 November", "Mon 9", "Mon, 09 Nov 2026", "Mock Test", "NRTS 05", "Official paper", 0)
        day++

        addTask("9–15 November", "Tue 10", "Tue, 10 Nov 2026", "Physics", "Work, Energy and Power", "", 30)
        day++

        addTask("9–15 November", "Wed 11", "Wed, 11 Nov 2026", "Mock Test", "NRTS 06", "Official paper", 0)
        day++

        addTask("9–15 November", "Thu 12", "Thu, 12 Nov 2026", "Physics", "Centre of Mass", "Morning mechanics", 25)
        addTask("9–15 November", "Thu 12", "Thu, 12 Nov 2026", "Zoology", "Chemical Coordination", "Afternoon hormone table", 40)
        day++

        addTask("9–15 November", "Fri 13", "Fri, 13 Nov 2026", "Mock Test", "Yakeen NEET-5", "Official paper", 0)
        day++

        addTask("9–15 November", "Sat 14", "Sat, 14 Nov 2026", "Physics", "Rotational Motion", "Torque, moment of inertia, rolling", 30)
        day++

        addTask("9–15 November", "Sun 15", "Sun, 15 Nov 2026", "Mock Test", "Yakeen AITS-2", "Official paper", 0)
        day++

        // =========================================================================
        // Week 6: 16–22 November
        // =========================================================================
        addTask("16–22 November", "Mon 16", "Mon, 16 Nov 2026", "Chemistry", "Periodicity", "", 25)
        addTask("16–22 November", "Mon 16", "Mon, 16 Nov 2026", "Botany", "Anatomy of Flowering Plants", "", 40)
        day++

        addTask("16–22 November", "Tue 17", "Tue, 17 Nov 2026", "Zoology", "Neural Control", "Neuron, impulse, synapse, brain, eye, ear", 45)
        day++

        addTask("16–22 November", "Wed 18", "Wed, 18 Nov 2026", "Mock Test", "NRTS 07", "Official paper", 0)
        day++

        addTask("16–22 November", "Thu 19", "Thu, 19 Nov 2026", "Physics", "Gravitation", "", 30)
        day++

        addTask("16–22 November", "Fri 20", "Fri, 20 Nov 2026", "Chemistry", "Chemical Bonding", "VSEPR, hybridisation, MOT of O2 and N2", 30)
        day++

        addTask("16–22 November", "Sat 21", "Sat, 21 Nov 2026", "Physics", "Mechanical Properties of Solids", "Last new work before the paper", 25)
        addTask("16–22 November", "Sat 21", "Sat, 21 Nov 2026", "Botany", "Respiration in Plants", "Last new work before the paper", 40)
        day++

        // 22 NOVEMBER MILESTONE!
        addTask("16–22 November", "Sun 22", "Sun, 22 Nov 2026", "Mock Test", "NRTS 08 • Phase-1 Major Milestone", "Major Milestone! Phase-1 Complete. Nothing new.", 0, isMilestone = true)
        day++

        // =========================================================================
        // Week 7: 23–29 November
        // =========================================================================
        addTask("23–29 November", "Mon 23", "Mon, 23 Nov 2026", "Physics", "Fluids", "No exam, so Sunday has a gap", 25)
        day++

        addTask("23–29 November", "Tue 24", "Tue, 24 Nov 2026", "Mock Test", "Yakeen NEET-6", "Same syllabus as NRTS 08", 0)
        day++

        addTask("23–29 November", "Wed 25", "Wed, 25 Nov 2026", "Chemistry", "IUPAC Nomenclature Only", "", 30)
        day++

        addTask("23–29 November", "Thu 26", "Thu, 26 Nov 2026", "Botany", "Photosynthesis", "Light reaction, then C3, C4, CAM, photorespiration. Two sittings", 40)
        day++

        addTask("23–29 November", "Fri 27", "Fri, 27 Nov 2026", "Zoology", "Animal Kingdom", "Non-chordates, then chordates. One chapter", 45)
        day++

        addTask("23–29 November", "Sat 28", "Sat, 28 Nov 2026", "Physics", "Thermal Properties of Matter", "", 25)
        day++

        addTask("23–29 November", "Sun 29", "Sun, 29 Nov 2026", "Mock Test", "NRTS 09", "Official paper", 0)
        day++

        // =========================================================================
        // Week 8: 30 November – 6 December
        // =========================================================================
        addTask("30 November – 6 December", "Mon 30", "Mon, 30 Nov 2026", "Physics", "Kinetic Theory", "", 25)
        addTask("30 November – 6 December", "Mon 30", "Mon, 30 Nov 2026", "Chemistry", "Isomerism", "", 30)
        day++

        addTask("30 November – 6 December", "Tue 1 Dec", "Tue, 01 Dec 2026", "Physics", "Thermodynamics", "", 30)
        day++

        addTask("30 November – 6 December", "Wed 2 Dec", "Wed, 02 Dec 2026", "Biology", "Biomolecules", "", 40)
        day++

        addTask("30 November – 6 December", "Thu 3 Dec", "Thu, 03 Dec 2026", "Botany", "Plant Growth and Development", "", 40)
        addTask("30 November – 6 December", "Thu 3 Dec", "Thu, 03 Dec 2026", "Zoology", "Reproductive Health", "", 35)
        day++

        addTask("30 November – 6 December", "Fri 4 Dec", "Fri, 04 Dec 2026", "Chemistry", "GOC", "Inductive, resonance, hyperconjugation, intermediates, reaction types", 30)
        day++

        addTask("30 November – 6 December", "Sat 5 Dec", "Sat, 05 Dec 2026", "Physics", "Oscillations", "", 30)
        addTask("30 November – 6 December", "Sat 5 Dec", "Sat, 05 Dec 2026", "Chemistry", "Hydrocarbons", "", 30)
        day++

        addTask("30 November – 6 December", "Sun 6 Dec", "Sun, 06 Dec 2026", "Mock Test", "Yakeen Rank Booster 3", "Phase-1 chapters only", 0)
        day++

        // =========================================================================
        // Week 9: 7–13 December
        // =========================================================================
        addTask("7–13 December", "Mon 7", "Mon, 07 Dec 2026", "Physics", "Waves", "", 30)
        addTask("7–13 December", "Mon 7", "Mon, 07 Dec 2026", "Chemistry", "Haloalkanes and Haloarenes", "", 30)
        day++

        addTask("7–13 December", "Tue 8", "Tue, 08 Dec 2026", "Zoology", "Human Reproduction", "", 45)
        day++

        addTask("7–13 December", "Wed 9", "Wed, 09 Dec 2026", "Botany", "Sexual Reproduction in Flowering Plants", "", 40)
        addTask("7–13 December", "Wed 9", "Wed, 09 Dec 2026", "Chemistry", "Alcohols, Phenols and Ethers", "", 30)
        day++

        addTask("7–13 December", "Thu 10", "Thu, 10 Dec 2026", "Physics", "Electric Charges and Fields", "", 25)
        addTask("7–13 December", "Thu 10", "Thu, 10 Dec 2026", "Zoology", "Evolution", "", 40)
        day++

        addTask("7–13 December", "Fri 11", "Fri, 11 Dec 2026", "Physics", "Potential and Capacitance", "", 30)
        addTask("7–13 December", "Fri 11", "Fri, 11 Dec 2026", "Biology", "Microbes in Human Welfare", "", 40)
        day++

        addTask("7–13 December", "Sat 12", "Sat, 12 Dec 2026", "Zoology", "Principles of Inheritance", "Mendel, linkage, disorders, pedigree", 45)
        day++

        addTask("7–13 December", "Sun 13", "Sun, 13 Dec 2026", "Mock Test", "NRTS 10", "Official paper", 0)
        day++

        // =========================================================================
        // Week 10: 14–20 December
        // =========================================================================
        addTask("14–20 December", "Mon 14", "Mon, 14 Dec 2026", "Physics", "Current Electricity", "NRTS 10 errors first. Ohm, Kirchhoff, Wheatstone, potentiometer", 30)
        day++

        addTask("14–20 December", "Tue 15", "Tue, 15 Dec 2026", "Physics", "Moving Charges and Magnetism", "", 25)
        addTask("14–20 December", "Tue 15", "Tue, 15 Dec 2026", "Zoology", "Human Health and Disease", "", 40)
        day++

        addTask("14–20 December", "Wed 16", "Wed, 16 Dec 2026", "Mock Test", "Yakeen AITS-3", "Official paper", 0)
        day++

        addTask("14–20 December", "Thu 17", "Thu, 17 Dec 2026", "Physics", "Electromagnetic Induction", "", 30)
        addTask("14–20 December", "Thu 17", "Thu, 17 Dec 2026", "Biology", "Organisms and Populations", "", 40)
        day++

        addTask("14–20 December", "Fri 18", "Fri, 18 Dec 2026", "Chemistry", "Aldehydes, Ketones and Carboxylic Acids", "First 20 minutes: alcohol errors from 9 Dec", 30)
        day++

        addTask("14–20 December", "Sat 19", "Sat, 19 Dec 2026", "Physics", "Alternating Current", "", 30)
        addTask("14–20 December", "Sat 19", "Sat, 19 Dec 2026", "Biology", "Ecosystem", "", 40)
        day++

        addTask("14–20 December", "Sun 20", "Sun, 20 Dec 2026", "Physics", "Electromagnetic Waves", "", 20)
        addTask("14–20 December", "Sun 20", "Sun, 20 Dec 2026", "Chemistry", "Amines", "", 30)
        day++

        // =========================================================================
        // Week 11: 21–27 December
        // =========================================================================
        addTask("21–27 December", "Mon 21", "Mon, 21 Dec 2026", "Physics", "Ray Optics", "Mirrors, refraction, TIR, lenses, microscope, telescope", 30)
        day++

        addTask("21–27 December", "Tue 22", "Tue, 22 Dec 2026", "Physics", "Wave Optics", "", 25)
        addTask("21–27 December", "Tue 22", "Tue, 22 Dec 2026", "Chemistry", "d- and f-Block", "", 25)
        day++

        addTask("21–27 December", "Wed 23", "Wed, 23 Dec 2026", "Chemistry", "Coordination Compounds", "", 30)
        addTask("21–27 December", "Wed 23", "Wed, 23 Dec 2026", "Biology", "Biotechnology: Principles and Processes", "", 40)
        day++

        addTask("21–27 December", "Thu 24", "Thu, 24 Dec 2026", "Zoology", "Molecular Basis", "Replication, transcription, code, translation, lac operon", 45)
        day++

        addTask("21–27 December", "Fri 25", "Fri, 25 Dec 2026", "Physics", "Dual Nature", "Swap with Thursday if the house is busy", 25)
        addTask("21–27 December", "Fri 25", "Fri, 25 Dec 2026", "Biology", "Biotechnology and its Applications", "", 40)
        day++

        addTask("21–27 December", "Sat 26", "Sat, 26 Dec 2026", "Physics", "Magnetism and Matter", "", 25)
        addTask("21–27 December", "Sat 26", "Sat, 26 Dec 2026", "Physics", "Atoms", "", 25)
        day++

        addTask("21–27 December", "Sun 27", "Sun, 27 Dec 2026", "Mock Test", "NRTS 11", "Official paper", 0)
        day++

        // =========================================================================
        // Week 12: 28–31 December
        // =========================================================================
        addTask("28–31 December", "Mon 28", "Mon, 28 Dec 2026", "Physics", "Nuclei", "NRTS 11 errors first", 25)
        addTask("28–31 December", "Mon 28", "Mon, 28 Dec 2026", "Chemistry", "Biomolecules (Chemistry)", "Chemistry book, not a reread of the biology chapter", 25)
        day++

        addTask("28–31 December", "Tue 29", "Tue, 29 Dec 2026", "Physics", "Semiconductors", "", 30)
        addTask("28–31 December", "Tue 29", "Tue, 29 Dec 2026", "Chemistry", "Principles Related to Practical Chemistry", "", 20)
        day++

        addTask("28–31 December", "Wed 30", "Wed, 30 Dec 2026", "Mock Test", "Yakeen NEET-7", "Official paper", 0)
        day++

        addTask("28–31 December", "Thu 31", "Thu, 31 Dec 2026", "Physics", "Experimental Skills", "Last chapters. Syllabus closed!", 20)
        addTask("28–31 December", "Thu 31", "Thu, 31 Dec 2026", "Biology", "Biodiversity and Conservation", "Last chapters. Syllabus closed!", 40)

        return list
    }
}
