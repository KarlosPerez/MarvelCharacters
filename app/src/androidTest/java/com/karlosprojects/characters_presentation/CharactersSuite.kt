package com.karlosprojects.characters_presentation

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    CharacterDetailFragmentTest::class,
    CharactersFragmentTest::class,
    CharactersIntegrationTest::class
)
class CharactersSuite