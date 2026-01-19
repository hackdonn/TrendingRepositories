- Fetch and display a list of trending repositories from the provided API
- Each item should show:
  - Repository name
  - Owner's avatar (circular image)
  - Description (max 2 lines, ellipsize)
  - Star count with ⭐ icon
  - Language with a colored dot indicator
- Implement pull-to-refresh functionality
- Show appropriate loading state while fetching
- Handle empty state gracefully

#### 2. **Repository Detail Screen** (30 mins)

- Navigate to detail screen on item click
- Display:
  - Full repository name (`owner/repo`)
  - Owner avatar (larger)
  - Full description
  - Star count, Fork count, Watcher count
  - Primary language
  - Created date (formatted nicely)
  - "Open in Browser" button (opens GitHub URL)

#### 3. **Error Handling** (15 mins)

- Show user-friendly error messages for:
  - No internet connection
  - API failures
- Provide a "Retry" button on error states
- Errors should not crash the app

#### 4. **Search/Filter** (15 mins)

- Add a search bar to filter repositories by name (local filtering)
- Filter should be debounced (300ms)
- Show "No results found" when filter yields empty results

---

### Bonus Features (If Time Permits)

- [ ] Offline caching with Room (show cached data when offline)
- [ ] Dark mode support
- [ ] Skeleton loading animation
- [ ] Unit tests for ViewModel

---

## 🔌 API Details

### Endpoint

```
GET https://api.github.com/search/repositories?q=language:kotlin&sort=stars&order=desc&per_page=30
```

### Sample Response Structure

```json
{
  "items": [
    {
      "id": 123456,
      "name": "kotlin",
      "full_name": "JetBrains/kotlin",
      "owner": {
        "login": "JetBrains",
        "avatar_url": "https://avatars.githubusercontent.com/u/878437"
      },
      "html_url": "https://github.com/JetBrains/kotlin",
      "description": "The Kotlin Programming Language",
      "stargazers_count": 45000,
      "forks_count": 5500,
      "watchers_count": 45000,
      "language": "Kotlin",
      "created_at": "2012-02-13T17:29:58Z"
    }
  ]
}
```

---

## 🏗️ Technical Requirements

### Must Use

| Requirement       | Details                                 |
| ----------------- | --------------------------------------- |
| **Language**      | Kotlin                                  |
| **UI Framework**  | Jetpack Compose                         |
| **Architecture**  | MVVM with Clean Architecture principles |
| **Networking**    | Retrofit + OkHttp (or Ktor)             |
| **Image Loading** | Coil                                    |
| **Async**         | Kotlin Coroutines + Flow                |
| **DI**            | Hilt (or Koin)                          |
| **Navigation**    | Compose Navigation                      |

### Code Quality Expectations

- Separation of concerns (UI, Domain, Data layers)
- Proper state management in Compose
- No hardcoded strings (use string resources)
- Meaningful naming conventions
- Error handling at appropriate layers

---

## 📁 Expected Project Structure

```
app/
├── data/
│   ├── remote/
│   │   ├── api/
│   │   │   └── GitHubApi.kt
│   │   └── dto/
│   │       └── RepositoryDto.kt
│   └── repository/
│       └── RepoRepositoryImpl.kt
├── domain/
│   ├── model/
│   │   └── Repository.kt
│   ├── repository/
│   │   └── RepoRepository.kt
│   └── usecase/
│       └── GetTrendingReposUseCase.kt
├── presentation/
│   ├── list/
│   │   ├── RepoListScreen.kt
│   │   └── RepoListViewModel.kt
│   ├── detail/
│   │   ├── RepoDetailScreen.kt
│   │   └── RepoDetailViewModel.kt
│   └── components/
│       ├── RepoListItem.kt
│       ├── ErrorView.kt
│       └── LoadingView.kt
├── di/
│   └── AppModule.kt
└── navigation/
    └── AppNavigation.kt
