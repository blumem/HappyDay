package de.blumesladen.testdi

import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import app.cash.turbine.test
import de.blumesladen.data.local.database.AppDatabase
import de.blumesladen.data.local.database.DiaryEntry
import de.blumesladen.data.local.database.DiaryEntryDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class DiaryEntryDaoTest {
    @get: Rule
    val dispatcherRule = TestDispatcherRule()

    private lateinit var database: AppDatabase
    private lateinit var diaryEntryDao: DiaryEntryDao
    @Before
    fun setupDatabase() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()  // .allowMainThreadQueries().build() alternatively

        diaryEntryDao = database.diaryEntryDao()
    }

    @Test
    fun insertDiaryEntry_returnsTrue(): Unit = runTest { // runBlocking in case of .allowMainThreadQueries()
        val entry1 = DiaryEntry(
            uid = 1,
            forMyself = "aaaaaa",
            forOthers = "oooooo",
            unexpressedEmotions = "iiiiii")
        val entry2 = DiaryEntry(
            uid = 2,
            forMyself = "eee",
            forOthers = "uuu",
            unexpressedEmotions = "yyy")
        diaryEntryDao.insertDiaryEntry(entry1)
        diaryEntryDao.insertDiaryEntry(entry2)

        diaryEntryDao.getDiaryEntriesMostRecent().test {
            val list = awaitItem()
            Log.d("DAO_TEST" , "list is $list")
            assert(list.contains(entry1))
            assert(list.contains(entry2))
            cancel()
        }
    }

    @After
    fun closeDatabase() {
        database.close()
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class TestDispatcherRule(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
): TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}